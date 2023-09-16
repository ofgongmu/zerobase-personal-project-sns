package com.example.demo.service;

import com.example.demo.common.S3Component;
import com.example.demo.entity.Account;
import com.example.demo.entity.AccountDMRoom;
import com.example.demo.entity.DM;
import com.example.demo.entity.DMRoom;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.model.dm.AccountInfo;
import com.example.demo.model.dm.DMInfo;
import com.example.demo.model.dm.DMRoomContentResponseDto;
import com.example.demo.model.dm.DMRoomInfo;
import com.example.demo.model.dm.DMRoomInviteRequestDto;
import com.example.demo.model.dm.DMRoomInviteResponseDto;
import com.example.demo.model.dm.JoinedDMRoomsResponseDto;
import com.example.demo.model.dm.SendDMRequestDto;
import com.example.demo.repository.AccountDMRoomRepository;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.DMRepository;
import com.example.demo.repository.DMRoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class DMService {
  private final AccountRepository accountRepository;
  private final AccountDMRoomRepository accountDMRoomRepository;
  private final DMRoomRepository dmRoomRepository;
  private final DMRepository dmRepository;

  private final S3Component s3Component;
  private final SimpMessagingTemplate template;

  public DMRoomInviteResponseDto createDMRoom(String id, DMRoomInviteRequestDto request) {
    Account inviterAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));

    DMRoom createdDmRoom = new DMRoom();
    dmRoomRepository.save(createdDmRoom);
    accountDMRoomRepository.save(AccountDMRoom.builder().dmRoom(createdDmRoom).account(inviterAccount).build());

    StringBuilder invitedNicknames = new StringBuilder();
    invitedNicknames.append(inviterAccount.getNickname());

    List<Account> invitedAccounts = accountRepository.findAllByIdIn(request.getAccountIds());
    for (Account a: invitedAccounts) {
      accountDMRoomRepository.save(AccountDMRoom.builder().dmRoom(createdDmRoom).account(a).build());
      invitedNicknames.append(", ").append(a.getNickname());
    }

    template.convertAndSend("/sub/dm/room/" + createdDmRoom.getDmRoomNum(), invitedNicknames + "이(가) 입장했습니다.");

    return DMRoomInviteResponseDto.builder()
        .inviter(AccountInfo.fromEntity(inviterAccount))
        .invited(invitedAccounts.stream().map(AccountInfo::fromEntity).collect(Collectors.toList()))
        .build();
  }

  public JoinedDMRoomsResponseDto seeJoinedDmRooms(String id) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    List<DMRoomInfo> response = new ArrayList<>();
    List<DMRoom> dmRoomList = accountDMRoomRepository.findByAccountOrderRecent(account.getAccountNum()).stream()
        .map(AccountDMRoom::getDmRoom).toList();
    for (DMRoom room: dmRoomList) {
      List<Account> joinedAccounts = accountDMRoomRepository.findByDmRoom(room).stream()
          .map(AccountDMRoom::getAccount).toList();
      DM recentDM = dmRepository.findFirstByDmRoomOrderByCreatedDateDesc(room)
          .orElse(DM.builder().text("").build());
      response.add(
          DMRoomInfo.builder()
              .joinedNicknames(joinedAccounts.stream().map(Account::getNickname).collect(Collectors.toList()))
              .mostRecentDMText(recentDM.getText())
              .build());
    }
    return JoinedDMRoomsResponseDto.builder()
        .dmRoomList(response)
        .build();
  }

  public DMRoomContentResponseDto inviteToDMRoom(String id, Long dmRoomNum, DMRoomInviteRequestDto request) {
    Account inviterAccount = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    DMRoom dmRoom = dmRoomRepository.findByDmRoomNum(dmRoomNum)
        .orElseThrow(() -> new CustomException(ErrorCode.DM_ROOM_DOES_NOT_EXIST));
    checkIfInDMRoom(inviterAccount, dmRoom);

    StringBuilder invitedNicknames = new StringBuilder();
    List<Account> invitedAccounts = accountRepository.findAllByIdIn(request.getAccountIds());
    for (Account account: invitedAccounts) {
      accountDMRoomRepository.save(AccountDMRoom.builder()
          .account(account)
          .dmRoom(dmRoom)
          .build());
      invitedNicknames.append(account.getNickname()).append(", ");
    }

    template.convertAndSend("/sub/dm/room/" + dmRoom.getDmRoomNum(),
        invitedNicknames.substring(0, invitedNicknames.length() - 2) + "이(가) 입장했습니다.");

    return DMRoomContentResponseDto.builder()
        .dms(dmRoom.getDms().stream().map(DMInfo::fromEntity).collect(Collectors.toList()))
        .build();
  }

  public DMRoomContentResponseDto sendDM(String id, Long dmRoomNum, SendDMRequestDto request, MultipartFile image) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    DMRoom dmRoom = dmRoomRepository.findByDmRoomNum(dmRoomNum)
        .orElseThrow(() -> new CustomException(ErrorCode.DM_ROOM_DOES_NOT_EXIST));
    checkIfInDMRoom(account, dmRoom);

    if (request.getText() != null && request.getText().length() != 0) {
      template.convertAndSend("sub/dm/room/" + dmRoom.getDmRoomNum(), request.getText());
    }

    String imageUrl = null;
    if (image != null) {
      imageUrl = uploadDMImageIfExists(image);
      template.convertAndSend("sub/dm/room/" + dmRoom.getDmRoomNum(), imageUrl);
    }

    dmRoom.getDms().add(dmRepository.save(DM.builder()
        .account(account)
        .text(request.getText())
        .imageUrl(imageUrl)
        .build()));

    return DMRoomContentResponseDto.builder()
        .dms(dmRoom.getDms().stream().map(DMInfo::fromEntity).collect(Collectors.toList()))
        .build();
  }

  public DMRoomContentResponseDto seeDmRoom(String id, Long dmRoomNum) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    DMRoom dmRoom = dmRoomRepository.findByDmRoomNum(dmRoomNum)
        .orElseThrow(() -> new CustomException(ErrorCode.DM_ROOM_DOES_NOT_EXIST));
    checkIfInDMRoom(account, dmRoom);

    return DMRoomContentResponseDto.builder()
        .dms(dmRoom.getDms().stream().map(DMInfo::fromEntity).collect(Collectors.toList()))
        .build();
  }

  public void leaveDMRoom(String id, Long dmRoomNum) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    DMRoom dmRoom = dmRoomRepository.findByDmRoomNum(dmRoomNum)
        .orElseThrow(() -> new CustomException(ErrorCode.DM_ROOM_DOES_NOT_EXIST));
    checkIfInDMRoom(account, dmRoom);

    template.convertAndSend("/sub/dm/room/" + dmRoom.getDmRoomNum(),
        account.getNickname() + "이(가) 퇴장했습니다.");

    accountDMRoomRepository.deleteByAccountAndDmRoom(account, dmRoom);
  }



  private void checkIfInDMRoom(Account account, DMRoom dmRoom) {
    if (!accountDMRoomRepository.existsByAccountAndDmRoom(account, dmRoom)) {
      throw new CustomException(ErrorCode.NOT_INVITED_DM_ROOM);
    }
  }

  private String uploadDMImageIfExists(MultipartFile image) {
    if (image == null) {
      return null;
    }
    return s3Component.uploadFile("dm/", image);
  }
}

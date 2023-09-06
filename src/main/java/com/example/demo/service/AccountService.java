package com.example.demo.service;

import com.example.demo.common.MailComponent;
import com.example.demo.common.S3Component;
import com.example.demo.constants.FollowState;
import com.example.demo.entity.Account;
import com.example.demo.entity.Follow;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.model.AccountProtectionResponseDto;
import com.example.demo.model.AccountSearchRequestDto;
import com.example.demo.model.AccountSearchResponseDto;
import com.example.demo.model.AccountSetupRequestDto;
import com.example.demo.model.AccountSetupResponseDto;
import com.example.demo.model.FollowListResponseDto;
import com.example.demo.model.FollowResponseDto;
import com.example.demo.model.LogInRequestDto;
import com.example.demo.model.LogInResponseDto;
import com.example.demo.model.PendingListResponseDto;
import com.example.demo.model.SeeAccountResponseDto;
import com.example.demo.model.SignUpRequestDto;
import com.example.demo.model.SignUpResponseDto;
import com.example.demo.model.ValidEmailRequestDto;
import com.example.demo.model.ValidEmailResponseDto;
import com.example.demo.redis.DistributedLock;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FollowRepository;
import com.example.demo.security.AccountUserDetails;
import com.example.demo.security.TokenProvider;
import com.example.demo.util.PasswordUtil;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
  private final AccountRepository accountRepository;
  private final FollowRepository followRepository;

  private final TokenProvider tokenProvider;

  private final MailComponent mailComponent;
  private final S3Component s3Component;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return new AccountUserDetails(account);
  }
  public SignUpResponseDto signup(SignUpRequestDto request) {

    checkIfUnregisteredEmail(request.getEmail());

    String authCode = mailComponent.sendEmailWithCode(request.getEmail());

    return SignUpResponseDto.fromEntity(accountRepository.save(Account.builder()
        .email(request.getEmail())
        .password(PasswordUtil.encryptPassword(request.getPassword()))
        .authCode(authCode)
        .isActivated(false)
        .isProtected(false)
        .role("ROLE_USER")
        .build()));
  }

  public ValidEmailResponseDto validateEmail(ValidEmailRequestDto request) {

    Account account = accountRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.UNREGISTERED_EMAIL));
    checkCorrectAuthCode(account, request.getAuthCode());

    return ValidEmailResponseDto.fromEntity(accountRepository.save(account.toBuilder()
        .isActivated(true).build()));
  }

  @DistributedLock(key = "#key")
  public AccountSetupResponseDto accountSetup(final String key, AccountSetupRequestDto request, MultipartFile image) {

    Account account = accountRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.UNREGISTERED_EMAIL));
    checkIfActivatedAccount(account);
    checkIfUniqueId(request.getId());

    uploadImageIfExists(image, account);

    return AccountSetupResponseDto.fromEntity(accountRepository.save(account.toBuilder()
        .id(request.getId())
        .nickname(request.getNickname())
        .bio(request.getBio())
        .build()));
  }

  public LogInResponseDto login(LogInRequestDto request) {
    Account account = accountRepository.findById(request.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.INCORRECT_ACCOUNT_INFO));

    if (!PasswordUtil.equalsPassword(request.getPassword(), account.getPassword())) {
      throw new CustomException(ErrorCode.INCORRECT_ACCOUNT_INFO);
    }

    return LogInResponseDto.builder()
        .token(tokenProvider.createToken(account.getId(), account.getRole()))
        .build();
  }

  public AccountProtectionResponseDto changeAccountProtection(String id) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return AccountProtectionResponseDto.fromEntity(accountRepository.save(account.toBuilder()
        .isProtected(!account.getIsProtected())
        .build()));
  }

  public AccountSearchResponseDto searchAccount(AccountSearchRequestDto request) {
    return AccountSearchResponseDto.fromEntities(
        accountRepository.findByIdStartingWith(request.getKeyword()));
  }

  public SeeAccountResponseDto seeAccount(String id, String targetId) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Account targetAccount = accountRepository.findById(targetId)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));

    Optional<Follow> follow = followRepository.findByFollowingAndFollowed(account, targetAccount);

    /* TODO: 비공개 계정의 경우 포스트 안 보이게 할 것
    if (targetAccount.getIsProtected() &&
        (follow.isEmpty() || follow.get().getState() != FollowState.ACCEPTED)) {
    }
    */

    return SeeAccountResponseDto.fromEntity(targetAccount);

  }

  public FollowResponseDto follow(String id, String targetId) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Account targetAccount = accountRepository.findById(targetId)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));

    if (targetAccount.getIsProtected()) {
      return FollowResponseDto.fromEntity(followRepository.save(Follow.builder()
          .following(account)
          .followed(targetAccount)
          .state(FollowState.PENDING)
          .build()));
    }
    return FollowResponseDto.fromEntity(followRepository.save(Follow.builder()
        .following(account)
        .followed(targetAccount)
        .state(FollowState.ACCEPTED)
        .build()));
  }

  public void unfollow(String id, String targetId) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Account targetAccount = accountRepository.findById(targetId)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Follow follow = followRepository.findByFollowingAndFollowed(account, targetAccount)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOLLOWED_ACCOUNT));
    followRepository.delete(follow);
  }

  public PendingListResponseDto seePendingList(String id) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return PendingListResponseDto.fromEntities(
        followRepository.findByFollowedAndState(account, FollowState.PENDING));
  }

  public FollowResponseDto acceptFollow(String id, String targetId) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Account targetAccount = accountRepository.findById(targetId)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Follow follow = followRepository.findByFollowingAndFollowed(targetAccount, account)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOLLOWED_ACCOUNT));
    return FollowResponseDto.fromEntity(followRepository.save(follow.toBuilder()
        .state(FollowState.ACCEPTED)
        .build()));
  }

  public FollowListResponseDto seeFollowingList(String id) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return FollowListResponseDto.followingFromEntities(
        followRepository.findByFollowingAndState(account, FollowState.ACCEPTED));
  }

  public FollowListResponseDto seeFollowersList(String id) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    return FollowListResponseDto.followersFromEntities(
        followRepository.findByFollowedAndState(account, FollowState.ACCEPTED));
  }


  private void checkIfUnregisteredEmail(String email) {
    if (accountRepository.existsByEmail(email)) {
      throw new CustomException(ErrorCode.ALREADY_REGISTERED_EMAIL);
    }
  }

  private void checkCorrectAuthCode(Account account, String authCode) {
    if (!account.getAuthCode().equals(authCode)) {
      throw new CustomException(ErrorCode.AUTH_CODE_DOES_NOT_MATCH);
    }
  }

  private void checkIfActivatedAccount(Account account) {
    if (!account.getIsActivated()) {
      throw new CustomException(ErrorCode.INACTIVATED_ACCOUNT);
    }
  }

  private void checkIfUniqueId(String id) {
    if (accountRepository.existsById(id)) {
      throw new CustomException(ErrorCode.ID_ALREADY_EXISTS);
    }
  }

  private void uploadImageIfExists(MultipartFile image, Account account) {
    if (image != null) {
      accountRepository.save(account.toBuilder()
          .imageUrl(s3Component.uploadFile("profile-pic/", image))
          .build());
    }
  }


}

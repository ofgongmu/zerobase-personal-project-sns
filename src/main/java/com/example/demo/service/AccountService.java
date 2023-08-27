package com.example.demo.service;

import com.example.demo.common.MailComponent;
import com.example.demo.entity.Account;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.model.AccountSetupRequestDto;
import com.example.demo.model.AccountSetupResponseDto;
import com.example.demo.model.SignUpRequestDto;
import com.example.demo.model.SignUpResponseDto;
import com.example.demo.model.ValidEmailRequestDto;
import com.example.demo.model.ValidEmailResponseDto;
import com.example.demo.repository.AccountRepository;
import com.example.demo.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
  private final AccountRepository accountRepository;

  private final MailComponent mailComponent;
  public SignUpResponseDto signup(SignUpRequestDto request) {

    checkIfUnregisteredEmail(request.getEmail());

    String authCode = mailComponent.createCode();
    mailComponent.sendEmail(request.getEmail(), authCode);

    return SignUpResponseDto.fromEntity(accountRepository.save(Account.builder()
        .email(request.getEmail())
        .password(PasswordUtil.encryptPassword(request.getPassword()))
        .authCode(authCode)
        .isActivated(false)
        .build()));
  }

  public ValidEmailResponseDto validateEmail(ValidEmailRequestDto request) {

    Account account = accountRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.UNREGISTERED_EMAIL));
    checkCorrectAuthCode(account, request.getAuthCode());


    return ValidEmailResponseDto.fromEntity(accountRepository.save(account.toBuilder()
        .isActivated(true).build()));
  }

  public AccountSetupResponseDto accountSetup(AccountSetupRequestDto request) {

    Account account = accountRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.UNREGISTERED_EMAIL));
    checkIfActivatedAccount(account);
    checkIfUniqueId(request.getId());

    return AccountSetupResponseDto.fromEntity(accountRepository.save(account.toBuilder()
        .id(request.getId())
        .nickname(request.getNickname())
        .bio(request.getBio())
        .imageUrl(request.getImageUrl())
        .build()));
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
    if (!account.isActivated()) {
      throw new CustomException(ErrorCode.INACTIVATED_ACCOUNT);
    }
  }

  private void checkIfUniqueId(String id) {
    if (accountRepository.existsById(id)) {
      throw new CustomException(ErrorCode.ID_ALREADY_EXISTS);
    }
  }

}

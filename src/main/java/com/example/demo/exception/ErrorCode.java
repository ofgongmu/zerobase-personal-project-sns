package com.example.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  ALREADY_REGISTERED_EMAIL("이미 가입된 이메일입니다."),
  UNREGISTERED_EMAIL("존재하지 않는 계정입니다."),
  AUTH_CODE_DOES_NOT_MATCH("인증번호가 일치하지 않습니다."),
  INACTIVATED_ACCOUNT("인증이 되지 않았거나 비활성화된 계정입니다."),
  ID_ALREADY_EXISTS("이미 사용 중인 아이디입니다.");

  private final String message;
}

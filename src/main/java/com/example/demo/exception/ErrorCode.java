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
  ID_ALREADY_EXISTS("이미 사용 중인 아이디입니다."),
  INPUT_INVALID("입력값이 올바르지 않습니다."),
  ACCOUNT_DOES_NOT_EXIST("계정을 찾을 수 없습니다."),
  IMAGE_FORMAT_ERROR("이미지 형식이 올바르지 않습니다."),
  IMAGE_UPLOAD_FAILED("이미지 업로드에 실패했습니다."),
  INCORRECT_ACCOUNT_INFO("계정 정보가 일치하지 않습니다."),
  NOT_FOLLOWED_ACCOUNT("현재 팔로우하고 있지 않는 계정입니다."),
  POST_DOES_NOT_EXIST("존재하지 않는 포스트입니다."),
  IS_NOT_POST_WRITER("포스트를 삭제할 권한이 없습니다."),
  UNABLE_TO_SEE_POST("비공개 계정의 포스트는 팔로워만이 읽을 수 있습니다."),
  COMMENT_DOES_NOT_EXIST("존재하지 않는 댓글입니다."),
  IS_NOT_COMMENT_WRITER("댓글을 삭제할 권한이 없습니다."),
  DM_ROOM_DOES_NOT_EXIST("존재하지 않는 DM방입니다. "),
  NOT_INVITED_DM_ROOM("초대되지 않은 DM방입니다."),
  FAILED_TO_SEND_DM("DM 전송에 실패했습니다."),
  NOTIFICATION_DOES_NOT_EXIST("존재하지 않는 알림입니다."),
  IS_NOT_NOTIFICATION_RECEIVER("알림을 받은 당사자가 아닙니다."),
  ERROR_IN_NOTIFICATION("알림 오류가 있어 출처를 찾지 못했습니다."),
  CANNOT_FIND_DM("해당 DM을 찾을 수 없습니다.");

  private final String message;
}

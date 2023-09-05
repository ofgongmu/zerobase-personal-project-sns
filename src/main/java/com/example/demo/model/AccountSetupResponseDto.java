package com.example.demo.model;

import com.example.demo.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountSetupResponseDto {
  private String id;
  private String nickname;
  private String bio;

  public static AccountSetupResponseDto fromEntity(Account account) {
    return AccountSetupResponseDto.builder()
        .id(account.getId())
        .nickname(account.getNickname())
        .bio(account.getBio())
        .build();
  }
}

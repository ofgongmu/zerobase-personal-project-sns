package com.example.demo.model;

import com.example.demo.entity.Account;
import lombok.Builder;

@Builder
public class AccountSetupResponseDto {
  String id;
  String nickname;
  String bio;
  String imageUrl;

  public static AccountSetupResponseDto fromEntity(Account account) {
    return AccountSetupResponseDto.builder()
        .id(account.getId())
        .nickname(account.getNickname())
        .bio(account.getBio())
        .imageUrl(account.getImageUrl())
        .build();
  }
}

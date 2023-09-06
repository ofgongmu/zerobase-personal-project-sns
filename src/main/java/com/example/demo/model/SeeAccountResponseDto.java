package com.example.demo.model;

import com.example.demo.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeeAccountResponseDto {
  String id;
  String nickname;;
  String bio;
  String imageUrl;

  //TODO: POSTS -> protected: X, unprotected: O

  public static SeeAccountResponseDto fromEntity(Account account) {
    return SeeAccountResponseDto.builder()
        .id(account.getId())
        .nickname(account.getNickname())
        .bio(account.getBio())
        .imageUrl(account.getImageUrl())
        .build();
  }
}

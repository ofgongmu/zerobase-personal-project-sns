package com.example.demo.model.dm;

import com.example.demo.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountInfo {
  private String nickname;
  private String profilePicUrl;

  public static AccountInfo fromEntity(Account account) {
    return AccountInfo.builder()
        .nickname(account.getNickname())
        .profilePicUrl(account.getImageUrl())
        .build();
  }
}

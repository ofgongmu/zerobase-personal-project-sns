package com.example.demo.model.account;

import com.example.demo.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountProtectionResponseDto {
  private String id;
  private boolean isProtected;

  public static AccountProtectionResponseDto fromEntity(Account account) {
    return AccountProtectionResponseDto.builder()
        .id(account.getId())
        .isProtected(account.getIsProtected())
        .build();
  }
}

package com.example.demo.model.account;

import com.example.demo.entity.Account;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidEmailResponseDto {
  private String email;

  public static ValidEmailResponseDto fromEntity(Account account) {
    return ValidEmailResponseDto.builder()
        .email(account.getEmail())
        .build();
  }
}

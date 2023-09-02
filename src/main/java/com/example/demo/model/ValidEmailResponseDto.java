package com.example.demo.model;

import com.example.demo.entity.Account;
import lombok.Builder;

@Builder
public class ValidEmailResponseDto {
  String email;

  public static ValidEmailResponseDto fromEntity(Account account) {
    return ValidEmailResponseDto.builder()
        .email(account.getEmail())
        .build();
  }
}

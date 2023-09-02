package com.example.demo.model;

import com.example.demo.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponseDto {
  String email;

  public static SignUpResponseDto fromEntity(Account account) {
    return SignUpResponseDto.builder()
        .email(account.getEmail())
        .build();
  }
}

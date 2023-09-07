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
public class SignUpResponseDto {
  private String email;

  public static SignUpResponseDto fromEntity(Account account) {
    return SignUpResponseDto.builder()
        .email(account.getEmail())
        .build();
  }
}

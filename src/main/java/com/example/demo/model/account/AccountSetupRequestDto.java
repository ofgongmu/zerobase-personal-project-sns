package com.example.demo.model.account;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountSetupRequestDto {
  private String email;
  private String id;
  @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하여야 합니다.")
  private String nickname;
  private String bio;
}

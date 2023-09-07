package com.example.demo.model.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {
  @Email
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;

  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
  private String password;
}

package com.example.demo.model.account;

import lombok.Getter;

@Getter
public class ValidEmailRequestDto {
  private String email;
  private String authCode;
}

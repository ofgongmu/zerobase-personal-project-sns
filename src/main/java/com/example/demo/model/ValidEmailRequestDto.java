package com.example.demo.model;

import lombok.Getter;

@Getter
public class ValidEmailRequestDto {
  private String email;
  private String authCode;
}

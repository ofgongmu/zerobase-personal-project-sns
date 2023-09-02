package com.example.demo.model;

import lombok.Getter;

@Getter
public class ValidEmailRequestDto {
  String email;
  String authCode;
}

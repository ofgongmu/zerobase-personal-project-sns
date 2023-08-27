package com.example.demo.model;

import lombok.Getter;

@Getter
public class AccountSetupRequestDto {
  String email;
  String id;
  String nickname;
  String bio;
  String imageUrl;
}

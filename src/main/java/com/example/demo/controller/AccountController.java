package com.example.demo.controller;

import com.example.demo.model.AccountSetupRequestDto;
import com.example.demo.model.SignUpRequestDto;
import com.example.demo.model.ValidEmailRequestDto;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccountController {
  private final AccountService accountService;
  @PostMapping("/account/signup")
  public ResponseEntity<?> signup(@RequestBody SignUpRequestDto request) {
    return ResponseEntity.ok(accountService.signup(request));
  }

  @PostMapping("/account/email-validation")
  public ResponseEntity<?> validateEmail(@RequestBody ValidEmailRequestDto request) {
    return ResponseEntity.ok(accountService.validateEmail(request));
  }

  @PostMapping("/account/set-account")
  public ResponseEntity<?> accountSetup(@RequestBody AccountSetupRequestDto request) {
    return ResponseEntity.ok(accountService.accountSetup(request));
  }
}

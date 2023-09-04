package com.example.demo.controller;

import com.example.demo.model.AccountSetupRequestDto;
import com.example.demo.model.SignUpRequestDto;
import com.example.demo.model.ValidEmailRequestDto;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
  private final AccountService accountService;

  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequestDto request) {
    return ResponseEntity.ok(accountService.signup(request));
  }

  @PostMapping("/email-validation")
  public ResponseEntity<?> validateEmail(@RequestBody ValidEmailRequestDto request) {
    return ResponseEntity.ok(accountService.validateEmail(request));
  }

  @PostMapping("/set-account")
  public ResponseEntity<?> accountSetup(@RequestPart(value = "request") @Valid AccountSetupRequestDto request,
                                        @RequestPart(value = "image", required = false) MultipartFile image) {
    return ResponseEntity.ok(accountService.accountSetup(request.getId(), request, image));
  }
}

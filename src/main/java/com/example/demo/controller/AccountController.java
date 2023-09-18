package com.example.demo.controller;

import com.example.demo.model.account.AccountSearchRequestDto;
import com.example.demo.model.account.AccountSetupRequestDto;
import com.example.demo.model.account.LogInRequestDto;
import com.example.demo.model.account.SignUpRequestDto;
import com.example.demo.model.account.ValidEmailRequestDto;
import com.example.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/login")
  public ResponseEntity<?> login(@RequestBody LogInRequestDto request) {
    return ResponseEntity.ok(accountService.login(request));
  }

  @PutMapping("/protected")
  public ResponseEntity<?> changeAccountProtection(@AuthenticationPrincipal String id) {
    return ResponseEntity.ok(accountService.changeAccountProtection(id));
  }

  @GetMapping("/search")
  public ResponseEntity<?> searchAccount(@RequestBody AccountSearchRequestDto request) {
    return ResponseEntity.ok(accountService.searchAccount(request));
  }

  @GetMapping("/{targetId}")
  public ResponseEntity<?> seeAccount(@AuthenticationPrincipal String id, @PathVariable String targetId,
                                      @RequestParam(required = false) Long lastPostNum) {
    return ResponseEntity.ok(accountService.seeAccount(id, targetId, lastPostNum));
  }

  @PostMapping("/{targetId}")
  public ResponseEntity<?> follow(@AuthenticationPrincipal String id, @PathVariable String targetId) {
    return ResponseEntity.ok(accountService.follow(id, targetId));
  }

  @DeleteMapping("/{targetId}")
  public void unfollow(@AuthenticationPrincipal String id, @PathVariable String targetId) {
    accountService.unfollow(id, targetId);
  }

  @GetMapping("/pending")
  public ResponseEntity<?> seePendingList(@AuthenticationPrincipal String id) {
    return ResponseEntity.ok(accountService.seePendingList(id));
  }

  @PutMapping("/pending/{targetId}")
  public ResponseEntity<?> acceptFollow(@AuthenticationPrincipal String id, @PathVariable String targetId) {
    return ResponseEntity.ok(accountService.acceptFollow(id, targetId));
  }

  @GetMapping("/following")
  public ResponseEntity<?> seeFollowingList(@AuthenticationPrincipal String id) {
    return ResponseEntity.ok(accountService.seeFollowingList(id));
  }

  @GetMapping("/followers")
  public ResponseEntity<?> seeFollowersList(@AuthenticationPrincipal String id) {
    return ResponseEntity.ok(accountService.seeFollowersList(id));
  }
}

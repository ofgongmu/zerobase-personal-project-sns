package com.example.demo.controller;

import com.example.demo.service.NotificationService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
  private final NotificationService notificationService;


  @GetMapping("/notifications")
  public ResponseEntity<?> seeNotificationsList(@AuthenticationPrincipal String id, @RequestParam Long lastNotiNum) {
    return ResponseEntity.ok(notificationService.seeNotificationList(id, lastNotiNum));
  }

  @GetMapping("/notifications/{notificationNum}")
  public ResponseEntity<?> moveToContentOnNotification(@AuthenticationPrincipal String id, @PathVariable Long notificationNum) {
    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(URI.create(notificationService.moveToContent(id, notificationNum)));
    return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
  }
}

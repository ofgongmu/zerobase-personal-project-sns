package com.example.demo.controller;

import com.example.demo.model.feed.FeedRequestDto;
import com.example.demo.model.feed.WritePostRequestDto;
import com.example.demo.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FeedController {
  private final FeedService feedService;

  @PostMapping("/post/write")
  public ResponseEntity<?> writePost(@AuthenticationPrincipal String id,
                                    @RequestPart(value = "request") @Valid WritePostRequestDto request,
                                    @RequestPart(value = "image", required = false) MultipartFile image) {
    return ResponseEntity.ok(feedService.writePost(id, request, image));
  }

  @DeleteMapping("/post/{postNum}")
  public void deletePost(@AuthenticationPrincipal String id, @PathVariable Long postNum) {
    feedService.deletePost(id, postNum);
  }

  @GetMapping("/post/{postNum}")
  public ResponseEntity<?> seePost(@AuthenticationPrincipal String id, @PathVariable Long postNum) {
    return ResponseEntity.ok(feedService.seePost(id, postNum));
  }

  @GetMapping("/feed")
  public ResponseEntity<?> seeFeed(@AuthenticationPrincipal String id, @RequestBody FeedRequestDto request) {
    return ResponseEntity.ok(feedService.seeFeed(id, request));
  }
}

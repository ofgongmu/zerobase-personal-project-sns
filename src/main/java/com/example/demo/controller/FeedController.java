package com.example.demo.controller;

import com.example.demo.model.feed.WriteRequestDto;
import com.example.demo.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class FeedController {
  private final FeedService feedService;

  @PostMapping("/post/write")
  public ResponseEntity<?> writePost(@AuthenticationPrincipal String id,
                                    @RequestPart(value = "request") @Valid WriteRequestDto request,
                                    @RequestPart(value = "image", required = false) MultipartFile image) {
    return ResponseEntity.ok(feedService.writePost(id, request, image));
  }

  @GetMapping("/post/write")
  public ResponseEntity<?> suggestTag(@RequestParam String tag) {
    return ResponseEntity.ok(feedService.suggestTag(tag));
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
  public ResponseEntity<?> seeFeed(@AuthenticationPrincipal String id, @RequestParam Long lastPostNum) {
    return ResponseEntity.ok(feedService.seeFeed(id, lastPostNum));
  }

  @PostMapping("/post/{postNum}/comment")
  public ResponseEntity<?> writeComment(@AuthenticationPrincipal String id, @PathVariable Long postNum,
                                        @RequestPart(value = "request") @Valid WriteRequestDto request,
                                        @RequestPart(value = "image", required = false) MultipartFile image) {
    return ResponseEntity.ok(feedService.writeComment(id, postNum, request, image));
  }

  @GetMapping("/post/{postNum}/comment")
  public ResponseEntity<?> suggestTag(@PathVariable Long postNum, @RequestParam String tag) {
    return ResponseEntity.ok(feedService.suggestTag(tag));
  }

  @DeleteMapping("/comment/{commentNum}")
  public void deleteComment(@AuthenticationPrincipal String id, @PathVariable Long commentNum) {
    feedService.deleteComment(id, commentNum);
  }

  @GetMapping("/comment/{commentNum}")
  public ResponseEntity<?> seeComment(@AuthenticationPrincipal String id, @PathVariable Long commentNum) {
    return ResponseEntity.ok(feedService.seeComment(id, commentNum));
  }

  @GetMapping("/search")
  public ResponseEntity<?> search(@AuthenticationPrincipal String id,
                                  @RequestParam String keyword, @RequestParam int page) {
    return ResponseEntity.ok(feedService.search(id, keyword, page));
  }
}

package com.example.demo.model.feed;

import com.example.demo.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WritePostResponseDto {
  String text;
  String imageUrl;
  LocalDateTime createdDate;

  public static WritePostResponseDto fromEntity(Post post) {
    return WritePostResponseDto.builder()
        .text(post.getText())
        .imageUrl(post.getImageUrl())
        .createdDate(post.getCreatedDate())
        .build();
  }
}

package com.example.demo.model.feed;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WriteResponseDto {
  String text;
  String imageUrl;
  LocalDateTime createdDate;

  public static WriteResponseDto fromEntity(Post post) {
    return WriteResponseDto.builder()
        .text(post.getText())
        .imageUrl(post.getImageUrl())
        .createdDate(post.getCreatedDate())
        .build();
  }

  public static WriteResponseDto fromEntity(Comment comment) {
    return WriteResponseDto.builder()
        .text(comment.getText())
        .imageUrl(comment.getImageUrl())
        .createdDate(comment.getCreatedDate())
        .build();
  }
}

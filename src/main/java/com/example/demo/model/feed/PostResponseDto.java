package com.example.demo.model.feed;

import com.example.demo.entity.Post;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
  private String writerId;
  private String text;
  private String imageUrl;
  private LocalDateTime createdDate;

  public static PostResponseDto fromEntity(Post post) {
    return PostResponseDto.builder()
        .writerId(post.getAccount().getId())
        .text(post.getText())
        .imageUrl(post.getImageUrl())
        .createdDate(post.getCreatedDate())
        .build();
  }
}

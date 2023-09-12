package com.example.demo.model.feed;

import com.example.demo.entity.Post;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeePostResponseDto {
  private String writerId;
  private String text;
  private String imageUrl;
  private LocalDateTime createdDate;
  private List<CommentResponseDto> comments;

  public static SeePostResponseDto fromEntity(Post post) {
    return SeePostResponseDto.builder()
        .writerId(post.getAccount().getId())
        .text(post.getText())
        .imageUrl(post.getImageUrl())
        .createdDate(post.getCreatedDate())
        .build();
  }

  public static SeePostResponseDto fromEntityClosed(Post post) {
    return SeePostResponseDto.builder()
        .writerId(post.getAccount().getId())
        .text("비공개 포스트입니다.")
        .createdDate(post.getCreatedDate())
        .build();
  }
}

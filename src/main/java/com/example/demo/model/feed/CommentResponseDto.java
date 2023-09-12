package com.example.demo.model.feed;

import com.example.demo.entity.Comment;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
  private String writerId;
  private String text;
  private String imageUrl;
  private LocalDateTime createdDate;

  public static CommentResponseDto fromEntity(Comment comment) {
    return CommentResponseDto.builder()
        .writerId(comment.getAccount().getId())
        .text(comment.getText())
        .imageUrl(comment.getImageUrl())
        .createdDate(comment.getCreatedDate())
        .build();
  }

  public static CommentResponseDto fromEntityClosed(Comment comment) {
    return CommentResponseDto.builder()
        .writerId(comment.getAccount().getId())
        .text("비공개 댓글입니다.")
        .createdDate(comment.getCreatedDate())
        .build();
  }

}

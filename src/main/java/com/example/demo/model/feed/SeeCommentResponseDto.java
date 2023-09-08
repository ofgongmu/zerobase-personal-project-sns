package com.example.demo.model.feed;

import com.example.demo.entity.Comment;
import java.time.LocalDateTime;
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
public class SeeCommentResponseDto {
  private SeePostResponseDto post;
  private String writerId;
  private String text;
  private String imageUrl;
  private LocalDateTime createdDate;

  public static SeeCommentResponseDto fromEntity(Comment comment) {
    return SeeCommentResponseDto.builder()
        .post(SeePostResponseDto.fromEntity(comment.getPost()))
        .writerId(comment.getAccount().getId())
        .text(comment.getText())
        .imageUrl(comment.getImageUrl())
        .createdDate(comment.getCreatedDate())
        .build();
  }

  public static SeeCommentResponseDto fromEntityClosed(Comment comment) {
    return SeeCommentResponseDto.builder()
        .post(SeePostResponseDto.fromEntity(comment.getPost()))
        .writerId(comment.getAccount().getId())
        .text("비공개 댓글입니다.")
        .createdDate(comment.getCreatedDate())
        .build();
  }
}

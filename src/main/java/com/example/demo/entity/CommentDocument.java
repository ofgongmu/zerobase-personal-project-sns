package com.example.demo.entity;

import com.example.demo.util.LocalDateTimeConverter;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;

@Document(indexName = "content")
@Setter
@Builder
public class CommentDocument {
  @Id
  String documentId;
  @Field(type = FieldType.Long)
  Long commentNum;
  String accountNickName;
  String accountId;
  String profileImageUrl;
  @Field(type = FieldType.Nested)
  PostDocument postDocument;
  @Field(type = FieldType.Text)
  String text;
  String imageUrl;
  @ValueConverter(LocalDateTimeConverter.class)
  LocalDateTime createdDate;

  public static CommentDocument fromComment(Comment comment) {
    return CommentDocument.builder()
        .commentNum(comment.getCommentNum())
        .accountNickName(comment.getAccount().getNickname())
        .accountId(comment.getAccount().getId())
        .profileImageUrl(comment.getAccount().getImageUrl())
        .postDocument(PostDocument.fromPost(comment.getPost()))
        .text(comment.getText())
        .imageUrl(comment.getImageUrl())
        .createdDate(comment.getCreatedDate())
        .build();
  }
}

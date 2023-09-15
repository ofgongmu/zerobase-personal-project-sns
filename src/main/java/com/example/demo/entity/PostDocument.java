package com.example.demo.entity;

import com.example.demo.util.LocalDateTimeConverter;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.ValueConverter;

@Document(indexName = "content")
@Getter
@Builder
public class PostDocument {
  @Id
  private String documentId;
  @Field(type = FieldType.Long)
  private Long postNum;
  private String accountNickName;
  private String accountId;
  private String profileImageUrl;
  @Field(type = FieldType.Text)
  private String text;
  private String imageUrl;
  @ValueConverter(LocalDateTimeConverter.class)
  private LocalDateTime createdDate;

  public static PostDocument fromPost(Post post) {
    return PostDocument.builder()
        .postNum(post.getPostNum())
        .accountNickName(post.getAccount().getNickname())
        .accountId(post.getAccount().getId())
        .profileImageUrl(post.getAccount().getImageUrl())
        .text(post.getText())
        .imageUrl(post.getImageUrl())
        .createdDate(post.getCreatedDate())
        .build();
  }
}

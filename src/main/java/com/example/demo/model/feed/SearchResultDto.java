package com.example.demo.model.feed;

import com.example.demo.util.LocalDateTimeConverter;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResultDto {
  private String accountNickname;
  private String accountId;
  private String profileImageUrl;
  private String text;
  private String imageUrl;
  private LocalDateTime createdDate;

  public static SearchResultDto fromContent(Content content) {
    return SearchResultDto.builder()
        .accountNickname(content.get("accountNickName").toString())
        .accountId(content.get("accountId").toString())
        .profileImageUrl(content.get("profileImageUrl") == null ? "" : content.get("profileImageUrl").toString())
        .text(content.get("text").toString())
        .imageUrl(content.get("imageUrl") == null ? "" : content.get("imageUrl").toString())
        .createdDate(LocalDateTimeConverter.toLocalDateTime((Integer) content.get("createdDate")))
        .build();
  }
}

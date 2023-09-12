package com.example.demo.model.feed;

import com.example.demo.entity.Post;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class FeedResponseDto {
  private List<SeePostResponseDto> feed;

  public static FeedResponseDto fromEntities(Page<Post> feedPostList) {
    return FeedResponseDto.builder()
        .feed(feedPostList.stream().map(SeePostResponseDto::fromEntity).collect(Collectors.toList()))
        .build();
  }
}

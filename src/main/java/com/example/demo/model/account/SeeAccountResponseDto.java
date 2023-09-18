package com.example.demo.model.account;

import com.example.demo.entity.Account;
import com.example.demo.entity.Post;
import com.example.demo.model.feed.PostResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class SeeAccountResponseDto {
  String id;
  String nickname;;
  String bio;
  String imageUrl;
  List<PostResponseDto> posts;

  public static SeeAccountResponseDto fromEntity(Account account, Page<Post> posts) {
    return SeeAccountResponseDto.builder()
        .id(account.getId())
        .nickname(account.getNickname())
        .bio(account.getBio())
        .imageUrl(account.getImageUrl())
        .posts(posts == null ? null : posts.stream().map(PostResponseDto::fromEntity).collect(Collectors.toList()))
        .build();
  }
}

package com.example.demo.model.account;

import com.example.demo.entity.Follow;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowListResponseDto {
  List<String> result;

  public static FollowListResponseDto followersFromEntities(List<Follow> followList) {
    List<String> result =
        followList.stream().map(f -> f.getFollowing().getId()).collect(Collectors.toList());
    return FollowListResponseDto.builder()
        .result(result)
        .build();
  }

  public static FollowListResponseDto followingFromEntities(List<Follow> followList) {
    List<String> result =
        followList.stream().map(f -> f.getFollowed().getId()).collect(Collectors.toList());
    return FollowListResponseDto.builder()
        .result(result)
        .build();
  }
}

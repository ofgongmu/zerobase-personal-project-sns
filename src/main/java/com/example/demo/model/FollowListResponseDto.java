package com.example.demo.model;

import com.example.demo.entity.Follow;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowListResponseDto {
  List<String> result;

  public static FollowListResponseDto followersFromEntities(List<Follow> followList) {
    List<String> result = new ArrayList<>();
    for(Follow follow: followList) {
      result.add(follow.getFollowing().getId());
    }
    return FollowListResponseDto.builder()
        .result(result)
        .build();
  }

  public static FollowListResponseDto followingFromEntities(List<Follow> followList) {
    List<String> result = new ArrayList<>();
    for(Follow follow: followList) {
      result.add(follow.getFollowed().getId());
    }
    return FollowListResponseDto.builder()
        .result(result)
        .build();
  }
}

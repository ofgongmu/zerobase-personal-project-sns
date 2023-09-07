package com.example.demo.model;

import com.example.demo.constants.FollowState;
import com.example.demo.entity.Follow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowResponseDto {
  private FollowState state;

  public static FollowResponseDto fromEntity(Follow follow) {
    return FollowResponseDto.builder()
        .state(follow.getState())
        .build();
  }
}

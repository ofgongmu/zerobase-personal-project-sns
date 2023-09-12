package com.example.demo.model.account;

import com.example.demo.entity.Follow;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PendingListResponseDto {
  List<String> result;

  public static PendingListResponseDto fromEntities(List<Follow> pendingList) {
    List<String> result = new ArrayList<>();
    for(Follow follow: pendingList) {
      result.add(follow.getFollowing().getId());
    }
    return PendingListResponseDto.builder()
        .result(result)
        .build();
  }
}

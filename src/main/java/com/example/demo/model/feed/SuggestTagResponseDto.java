package com.example.demo.model.feed;

import com.example.demo.entity.Account;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestTagResponseDto {
  private List<String> ids;

  public static SuggestTagResponseDto fromEntities(List<Account> ids) {
    return SuggestTagResponseDto.builder()
        .ids(ids.stream().map(Account::getId).collect(Collectors.toList()))
        .build();
  }
}

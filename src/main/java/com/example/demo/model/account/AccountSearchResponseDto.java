package com.example.demo.model.account;

import com.example.demo.entity.Account;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountSearchResponseDto {
  List<String> result;

  public static AccountSearchResponseDto fromEntities(List<Account> searchResult) {
    List<String> result = new ArrayList<>();
    for(Account account: searchResult) {
      result.add(account.getId());
    }
    return AccountSearchResponseDto.builder()
        .result(result)
        .build();
  };
}

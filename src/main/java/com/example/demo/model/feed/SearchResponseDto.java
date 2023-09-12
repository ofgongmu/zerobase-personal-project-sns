package com.example.demo.model.feed;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResponseDto {
  private List<SearchResultDto> resultList;
}

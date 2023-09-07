package com.example.demo.model.feed;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class WritePostRequestDto {
  @Size(min = 1, max = 150)
  private String text;
}

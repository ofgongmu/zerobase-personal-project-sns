package com.example.demo.entity;

import com.example.demo.constants.ContentType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Notification {
  private Long notificationNum;
  @ManyToOne
  private Account account;
  @Enumerated(value = EnumType.STRING)
  private ContentType contentType;
  private Long contentId;
  private String text;
}

package com.example.demo.model.notification;

import com.example.demo.entity.Notification;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponseDto {
  private String text;

  public static NotificationResponseDto fromEntity(Notification notification) {
    return NotificationResponseDto.builder()
        .text(notification.getText())
        .build();
  }
}

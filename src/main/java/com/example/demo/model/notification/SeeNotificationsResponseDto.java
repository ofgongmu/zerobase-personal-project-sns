package com.example.demo.model.notification;

import com.example.demo.entity.Notification;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class SeeNotificationsResponseDto {
  private List<NotificationResponseDto> notifications;

  public static SeeNotificationsResponseDto fromEntity(Page<Notification> notificationPage) {
    return SeeNotificationsResponseDto.builder()
        .notifications(notificationPage.stream().map(NotificationResponseDto::fromEntity)
            .collect(Collectors.toList()))
        .build();
  }
}

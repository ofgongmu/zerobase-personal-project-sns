package com.example.demo.service;

import com.example.demo.constants.ContentType;
import com.example.demo.entity.Account;
import com.example.demo.entity.DM;
import com.example.demo.entity.Notification;
import com.example.demo.exception.CustomException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.model.notification.SeeNotificationsResponseDto;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.DMRepository;
import com.example.demo.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final AccountRepository accountRepository;
  private final DMRepository dmRepository;
  private final NotificationRepository notificationRepository;

  private final SimpMessagingTemplate simpMessagingTemplate;

  @KafkaListener(topics = "tagNotification, dmNotification", groupId = "user-group")
  public void sendNotification(@Payload String message, @Header(KafkaHeaders.KEY) String key) {
    Account account = accountRepository.findById(key)
            .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    simpMessagingTemplate.convertAndSend("/sub/notifications/" + account.getAccountNum(), message);
  }

  @Transactional(readOnly = true)
  public SeeNotificationsResponseDto seeNotificationList(String id, Long lastNoti) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Page<Notification> notifications
        = notificationRepository.findByAccountAndNotificationNumLessThanOrderByNotificationNumDesc(
            account, lastNoti, PageRequest.of(0, 10));

    return SeeNotificationsResponseDto.fromEntity(notifications);
  }

  @Transactional(readOnly = true)
  public String moveToContent(String id, Long notificationNum) {
    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_DOES_NOT_EXIST));
    Notification notification = notificationRepository.findByNotificationNum(notificationNum)
        .orElseThrow(() -> new CustomException(ErrorCode.NOTIFICATION_DOES_NOT_EXIST));
    checkIfNotificationOwner(account, notification);
    return getRedirectionAddress(notification);
  }

  private void checkIfNotificationOwner(Account account, Notification notification) {
    if (!account.equals(notification.getAccount())) {
      throw new CustomException(ErrorCode.IS_NOT_NOTIFICATION_RECEIVER);
    }
  }

  private String getRedirectionAddress(Notification notification) {
    if (ContentType.POST.equals(notification.getContentType())) {
      return "/post/" + notification.getContentId();
    } else if (ContentType.COMMENT.equals(notification.getContentType())) {
      return "/comment/" + notification.getContentId();
    } else if (ContentType.DM.equals(notification.getContentType())) {
      DM dm = dmRepository.findByDmNum(notification.getContentId())
          .orElseThrow(() -> new CustomException(ErrorCode.CANNOT_FIND_DM));
      return "/room/" + dm.getDmRoom().getDmRoomNum();
    } else {
      throw new CustomException(ErrorCode.ERROR_IN_NOTIFICATION);
    }
  }
}

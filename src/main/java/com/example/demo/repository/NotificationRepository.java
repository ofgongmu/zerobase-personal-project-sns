package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Notification;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  Page<Notification> findByAccountAndNotificationNumLessThanOrderByNotificationNumDesc(
      Account account, Long lastNotiNum, PageRequest pageRequest);
  Optional<Notification> findByNotificationNum(Long notificationNum);
}

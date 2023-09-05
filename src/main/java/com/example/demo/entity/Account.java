package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountNum;

  @Column(unique = true)
  private String id;

  private String email;

  private String password;

  private String authCode;

  private Boolean isActivated;

  private String nickname;

  private String bio;

  private String imageUrl;

  private Boolean isProtected;

  private Boolean isPromotion;

  private String paymentInfo;

  private String role = "ROLE_USER";

  @CreatedDate
  private LocalDateTime createdDate;

  private LocalDateTime deactivatedDate;
}

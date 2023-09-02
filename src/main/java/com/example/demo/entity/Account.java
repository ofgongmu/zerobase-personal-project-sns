package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
  private long accountNum;

  @Column(unique = true)
  private String id;

  @Email
  @NotBlank(message = "이메일은 필수 입력값입니다.")
  private String email;

  @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
  private String password;

  private String authCode;

  private boolean isActivated;

  @Size(min = 1, max = 20, message = "닉네임은 1자 이상 20자 이하여야 합니다.")
  private String nickname;

  private String bio;

  private String imageUrl;

  private boolean isProtected;

  private boolean isPromotion;

  private String paymentInfo;

  @CreatedDate
  private LocalDateTime createdDate;

  private LocalDateTime deactivatedDate;
}

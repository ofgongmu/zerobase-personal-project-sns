package com.example.demo.entity;

import com.example.demo.constants.DMType;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class DM {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dmNum;
  @ManyToOne
  private DMRoom dmRoom;
  @ManyToOne
  private Account account;
  @Enumerated(value = EnumType.STRING)
  private DMType dmType;
  private String text;
  private String imageUrl;
  @CreatedDate
  private LocalDateTime createdDate;
}

package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDMRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long accountDMRoomNum;
  @ManyToOne
  private Account account;
  @ManyToOne
  private DMRoom dmRoom;
}

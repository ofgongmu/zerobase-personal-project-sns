package com.example.demo.repository;

import com.example.demo.entity.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
  boolean existsByEmail(String email);
  Optional<Account> findByEmail(String email);
  int countById(String id);
  boolean existsById(String id);
  Optional<Account> findById(String id);
  List<Account> findByIdStartingWith(String keyword);
  List<Account> findAllByIdIn(List<String> id);
}

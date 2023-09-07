package com.example.demo.repository;

import com.example.demo.constants.FollowState;
import com.example.demo.entity.Account;
import com.example.demo.entity.Follow;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowingAndFollowed(Account following, Account followed);
  List<Follow> findByFollowedAndState(Account followed, FollowState state);
  List<Follow> findByFollowingAndState(Account following, FollowState state);
}

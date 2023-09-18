package com.example.demo.repository;

import com.example.demo.entity.Account;
import com.example.demo.entity.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
  Optional<Post> findByPostNum(Long postNum);

  Page<Post> findByAccountAndPostNumLessThanOrderByPostNumDesc(Account account, Long postNum, PageRequest pageRequest);
  Page<Post> findByAccountIsInAndPostNumLessThanOrderByPostNumDesc(
      List<Account> followingList, Long lastPostNum, PageRequest pageRequest);
}

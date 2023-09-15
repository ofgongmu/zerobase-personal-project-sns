package com.example.demo.repository;

import com.example.demo.constants.ContentType;
import com.example.demo.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
  void deleteAllByContentTypeAndContentNum(ContentType type, Long contentNum);
}

package com.example.demo.repository;

import com.example.demo.constants.ContentType;
import com.example.demo.entity.Tag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
  List<Tag> findByContentTypeAndContentNum(ContentType type, Long contentNum);
}

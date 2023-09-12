package com.example.demo.repository;

import com.example.demo.entity.CommentDocument;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CommentDocumentRepository extends ElasticsearchRepository<CommentDocument, Long> {
  Optional<CommentDocument> findByCommentNum(Long commentNum);
  @Query("{\"term\":{\"postDocument.postNum\":\"?0\"}}")
  List<CommentDocument> findByPostDocumentNum(Long postNum);
  Page<CommentDocument> findByText(String keyword, Pageable pageable);
}

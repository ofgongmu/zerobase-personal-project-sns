package com.example.demo.repository;

import com.example.demo.entity.PostDocument;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, Long> {
  Optional<PostDocument> findByPostNum(Long postNum);
  Page<PostDocument> findByText(String keyword, Pageable pageable);

}

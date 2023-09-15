package com.example.demo.repository;

import com.example.demo.entity.PostDocument;
import java.util.Optional;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PostDocumentRepository extends ElasticsearchRepository<PostDocument, Long> {
  Optional<PostDocument> findByPostNum(Long postNum);
}

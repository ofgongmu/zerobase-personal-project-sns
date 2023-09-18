package com.example.demo.repository;

import com.example.demo.model.feed.Content;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ContentSearchRepository {
  private final ElasticsearchOperations elasticsearchOperations;

  public List<Content> findContentsByKeyword(String keyword, int page) {
    Query query = new CriteriaQuery(new Criteria("text").matches(keyword))
        .addSort(Sort.by("createdDate").descending()).setPageable(PageRequest.of(page, 100));
    SearchHits<Content> searchHits =
        elasticsearchOperations.search(query, Content.class, IndexCoordinates.of("content"));
    List<Content> contentList = new ArrayList<>();
    searchHits.forEach(searchHit -> {
      Content content =
          elasticsearchOperations.getElasticsearchConverter()
              .read(Content.class, Document.from(searchHit.getContent()));
      contentList.add(content);
    });
    return contentList;
  }
}

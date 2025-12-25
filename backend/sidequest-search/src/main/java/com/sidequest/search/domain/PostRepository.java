package com.sidequest.search.domain;

import com.sidequest.search.infrastructure.PostDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface PostRepository extends ElasticsearchRepository<PostDoc, String> {
    @Query("{\"bool\": {\"must\": [{\"term\": {\"status\": 1}}], \"should\": [{\"match\": {\"title\": {\"query\": \"?0\", \"operator\": \"or\"}}}, {\"match\": {\"content\": {\"query\": \"?1\", \"operator\": \"or\"}}}, {\"match\": {\"tags\": {\"query\": \"?2\", \"operator\": \"or\"}}}]}}")
    Page<PostDoc> findByKeyword(String title, String content, String tags, Pageable pageable);
    
    Page<PostDoc> findByAuthorIdAndStatus(Long authorId, Integer status, Pageable pageable);
}


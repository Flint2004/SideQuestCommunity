package com.sidequest.search.domain;

import com.sidequest.search.infrastructure.PostDoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface PostRepository extends ElasticsearchRepository<PostDoc, String> {
    @Query("{\"bool\": {\"must\": [{\"term\": {\"status\": 1}}, {\"bool\": {\"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"tags\": \"?1\"}}], \"minimum_should_match\": 1}}]}}")
    Page<PostDoc> findByKeyword(String title, String tags, Pageable pageable);
    
    Page<PostDoc> findByAuthorIdAndStatus(Long authorId, Integer status, Pageable pageable);
}


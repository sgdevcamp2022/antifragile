package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HashtagRepository extends MongoRepository<Hashtag, String> {

    Page<Hashtag> findByTagContaining(String tag, Pageable pageable);

    Optional<Hashtag> findByPostId(String post_id);

    Long deleteAllInBatchByPostId(String post_id);
}

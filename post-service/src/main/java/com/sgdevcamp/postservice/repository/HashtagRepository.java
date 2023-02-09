package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.Hashtag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HashtagRepository extends MongoRepository<Hashtag, String> {
    Optional<Hashtag> findByTag(String tag);
}

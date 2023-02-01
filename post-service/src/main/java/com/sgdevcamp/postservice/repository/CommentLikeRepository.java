package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.CommentLike;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentLikeRepository extends MongoRepository<CommentLike, String> {
}

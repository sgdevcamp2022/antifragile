package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.CommentLike;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentLikeRepository extends MongoRepository<CommentLike, String> {

    List<CommentLike> findAllByPostId(String post_id);

    void deleteAllInBatchByPostId(String post_id);
}

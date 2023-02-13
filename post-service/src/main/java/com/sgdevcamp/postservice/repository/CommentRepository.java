package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {

    Optional<Comment> findByPostId(String post_id);

    List<Comment> findAllByPostId(String post_id);

    void deleteByPostId(String post_id);

    void deleteAllInBatchByPostId(String post_id);
}

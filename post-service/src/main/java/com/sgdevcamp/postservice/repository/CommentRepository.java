package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    void deleteByPostId(String post_id);

    List<Comment> findAllByPostId(String post_id);
}

package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUsernameOrderByCreatedAtDesc(String username);

    List<Post> findByCreatedAtGreaterThanAndUserIdInOrderByCreatedAtDesc(Instant created_at, List<String> user_ids);

    List<Post> findByIdInOrderByCreatedAtDesc(List<String> ids);

    List<Post> findByUserIdInOrderByCreatedAtDesc(List<String> ids);

    int countByUsername(String username);
}

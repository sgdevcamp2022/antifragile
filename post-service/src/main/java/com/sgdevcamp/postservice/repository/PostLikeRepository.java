package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.PostLike;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostLikeRepository extends MongoRepository<PostLike, String> {
    void deleteByPostId(String post_id);

    void deleteByPostIdAndUsername(String post_id, String username);

}

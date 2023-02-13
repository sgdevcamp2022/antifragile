package com.sgdevcamp.postservice.repository;

import com.sgdevcamp.postservice.model.PostLike;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostLikeRepository extends MongoRepository<PostLike, String> {

    List<PostLike> findByPostId(String post_id);

    void deleteAllInBatchByPostId(String post_id);

    Long countByPostId(String post_id);

}

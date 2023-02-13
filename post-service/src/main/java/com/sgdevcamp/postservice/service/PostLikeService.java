package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.PostLike;
import com.sgdevcamp.postservice.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.NOT_FOUND_POSTLIKE;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    public String likePost(String post_id, String username){

        PostLike postLike = PostLike.builder()
                .postId(post_id)
                .build();

        PostLike save_postLike = postLikeRepository.save(postLike);

        log.info("user {} likes post {}", username, post_id);

        return save_postLike.getId();
    }

    public Long countLikePost(String post_id){

        if(postLikeRepository.findByPostId(post_id).isEmpty()) throw new CustomException(NOT_FOUND_POSTLIKE);

        return postLikeRepository.countByPostId(post_id);
    }

    public void deletePostLike(String like_id){

        postLikeRepository.findById(like_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POSTLIKE);});

        postLikeRepository.deleteById(like_id);
    }

    public void deleteAllPostLike(String post_id){

        if(postLikeRepository.findByPostId(post_id).isEmpty()) throw new CustomException(NOT_FOUND_POSTLIKE);

        postLikeRepository.deleteAllInBatchByPostId(post_id);
    }
}

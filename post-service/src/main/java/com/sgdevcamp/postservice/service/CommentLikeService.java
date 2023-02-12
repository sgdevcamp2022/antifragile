package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.model.CommentLike;
import com.sgdevcamp.postservice.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    public void likeComment(String post_id, String comment_id){

        CommentLike commentLike = CommentLike.builder()
                .postId(post_id)
                .build();

        commentLikeRepository.save(commentLike);

        log.info("save comment {} like of post {}", comment_id, post_id);
    }

    public void cancelCommentLike(String post_id, String comment_id){

        commentLikeRepository.deleteById(comment_id);

        log.info("delete comment {} of post {}", comment_id, post_id);
    }
}

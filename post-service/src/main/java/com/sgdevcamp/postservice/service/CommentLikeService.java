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

    public String likeComment(String post_id, String comment_id){

        CommentLike commentLike = CommentLike.builder()
                .commentId(comment_id)
                .postId(post_id)
                .build();

        CommentLike save_CommentLike = commentLikeRepository.save(commentLike);

        log.info("save comment {} like of post {}", comment_id, post_id);

        return save_CommentLike.getId();
    }

    public void cancelCommentLike(String post_id, String comment_id){

        commentLikeRepository.deleteById(comment_id);

        log.info("delete comment {} of post {}", comment_id, post_id);
    }

    public void cancelAllCommentLike(String post_id){
        if(commentLikeRepository.findByPostId(post_id).isPresent()) commentLikeRepository.deleteAllInBatchByPostId(post_id);
    }
}

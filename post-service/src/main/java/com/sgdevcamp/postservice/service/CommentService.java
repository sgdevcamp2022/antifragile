package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.dto.request.CommentCreateRequest;
import com.sgdevcamp.postservice.dto.request.CommentUpdateRequest;
import com.sgdevcamp.postservice.dto.response.CommentResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.Comment;
import com.sgdevcamp.postservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.NOT_FOUND_COMMENT;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponse createComment(CommentCreateRequest commentCreateRequest){

        Comment comment = commentCreateRequest.toEntity();

        Comment saved_comment = commentRepository.save(comment);

        CommentResponse commentResponse = saved_comment.toResponse();

        log.info("comment {} is saved successfully for user {}", saved_comment.getId(), saved_comment.getUsername());

        return commentResponse;
    }

    public boolean isExistComment(String id){
        return commentRepository.existsById(id);
    }

    public List<CommentResponse> findAllComment(String post_id){

        List<CommentResponse> responseList = commentRepository.findAllByPostId(post_id).stream()
                .map(Comment::toResponse)
                .collect(Collectors.toList());

        return responseList;
    }

    public CommentResponse updateComment(CommentUpdateRequest commentUpdateRequest){

        Comment saved_comment = commentRepository.findById(commentUpdateRequest.getId())
                .orElseThrow(() -> {
                    throw new CustomException(NOT_FOUND_COMMENT);
                });
        saved_comment.setContent(commentUpdateRequest.getContent());

        Comment comment = commentRepository.save(saved_comment);

        log.info("comment {} is updated successfully for user {}", saved_comment.getId(), saved_comment.getUsername());

        return comment.toResponse();
    }

    public void deleteComment(String comment_id){

        commentRepository.deleteById(comment_id);

        log.info("delete comment {}", comment_id);
    }

    public void deleteByPostId(String post_id){

        commentRepository.deleteByPostId(post_id);

        log.info("delete comment of post {}", post_id);
    }

}

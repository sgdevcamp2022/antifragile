package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.dto.request.CommentUpdateRequest;
import com.sgdevcamp.postservice.dto.request.PostRequest;
import com.sgdevcamp.postservice.dto.response.CommentResponse;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.messaging.PostEventSender;
import com.sgdevcamp.postservice.model.*;
import com.sgdevcamp.postservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ProfileRepository profileRepository;

    public PostResponse createPost(PostRequest postRequest){
        Post post = Post.builder()
                .images(postRequest.getImageUrl())
                .likeCount(0L)
                .commentCount(0)
                .content(postRequest.getContent())
                .hashTags(postRequest.getHashTags())
                .build();

        post = postRepository.save(post);

        PostResponse postResponse = PostResponse.builder()
                .username(post.getUsername())
                .images(post.getImages().stream().map(i -> i.getPath()).collect(Collectors.toList()))
                .content(post.getContent())
                .commentCount(0)
                .likeCount(0L)
                .hashTags(post.getHashTags())
                .createdAt(post.getCreatedAt())
                .build();

        log.info("post {} is saved successfully for user {}", post.getId(), post.getUsername());

        return postResponse;
    }

    public void createProfile(String username, String image){
        Profile profile = Profile.builder()
                .username(username)
                .image(image)
                .build();

        profileRepository.save(profile);
    }

    public void deletePost(String post_id, String username){
        postRepository
                .findById(post_id)
                .map(post ->{
                    if(!post.getUsername().equals(username)) throw new CustomException(NOT_ALLOWED_USER);
                    postRepository.delete(post);
                    commentRepository.deleteByPostId(post_id);
                    postLikeRepository.deleteByPostId(post_id);
                    log.info("deleting post {}", post_id);
                    return post;
                }).orElseThrow(() -> {
                    throw new CustomException(NOT_FOUND_POST);
                });
    }

    public List<PostResponse> postsByUsername(String username){
        return postRepository.findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(p -> PostResponse.builder()
                        .id(p.getId())
                        .username(p.getUsername())
                        .images(p.getImages().stream().map(i -> i.getPath()).collect(Collectors.toList()))
                        .content(p.getContent())
                        .commentCount(p.getCommentCount())
                        .likeCount(p.getLikeCount())
                        .hashTags(p.getHashTags())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public List<PostResponse> postsByIdIn(List<String> ids){
        return postRepository.findByIdInOrderByCreatedAtDesc(ids)
                .stream()
                .map(p -> PostResponse.builder()
                        .id(p.getId())
                        .username(p.getUsername())
                        .images(p.getImages().stream().map(i -> i.getPath()).collect(Collectors.toList()))
                        .content(p.getContent())
                        .commentCount(p.getCommentCount())
                        .likeCount(p.getLikeCount())
                        .hashTags(p.getHashTags())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public CommentResponse createComment(String post_id, CommentUpdateRequest commentUpdateRequest){
        Post saved_post = postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        Comment comment = Comment
                .builder()
                .postId(post_id)
                .content(commentUpdateRequest.getContent())
                .username(commentUpdateRequest.getUsername())
                .build();

        Comment saved_comment = commentRepository.save(comment);

        saved_post
                .setCommentCount(saved_post.getCommentCount() + 1);

        postRepository.save(saved_post);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(saved_comment.getId())
                .username(saved_comment.getUsername())
                .content(saved_comment.getContent())
                .createdAt(saved_comment.getCreatedAt())
                .build();

        log.info("comment {} is saved successfully for user {}", saved_comment.getId(), saved_comment.getUsername());

        return commentResponse;
    }

    public List<CommentResponse> findAllComment(String post_id){
        postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        List<CommentResponse> responseList = commentRepository.findAllByPostId(post_id).stream()
                .map(comment -> CommentResponse.builder()
                        .id(comment.getId())
                        .username(comment.getUsername())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }

    public void updateComment(String post_id, CommentUpdateRequest commentUpdateRequest){
        postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        Comment saved_comment = commentRepository.findById(commentUpdateRequest.getId())
                        .orElseThrow(() -> {
                           throw new CustomException(NOT_FOUND_COMMENT);
                        });
        saved_comment.setContent(commentUpdateRequest.getContent());

        commentRepository.save(saved_comment);

        log.info("comment {} is updated successfully for user {}", saved_comment.getId(), saved_comment.getUsername());
    }

    public void deleteComment(String comment_id, String post_id){
        Post saved_post = postRepository.findById(post_id)
                .orElseThrow(() ->{throw new CustomException(NOT_FOUND_POST);});

        saved_post
                .setCommentCount(saved_post.getCommentCount() - 1);

        postRepository.save(saved_post);

        commentRepository.deleteById(comment_id);
    }

    public void likePost(String post_id, String username){
        Post saved_post = postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        PostLike postLike = PostLike.builder()
                .postId(post_id)
                .username(username)
                .build();

        PostLike savedPostLike = postLikeRepository.save(postLike);
        saved_post.setLikeCount(saved_post.getLikeCount() + 1);

        postRepository.save(saved_post);

        log.info("{} likes {}", username, post_id);
    }

    public void cancelPostLike(String post_id, String username){
        Post saved_post = postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        postLikeRepository.deleteByPostIdAndUsername(post_id, username);

        saved_post.setLikeCount(saved_post.getLikeCount() - 1);

        postRepository.save(saved_post);

        log.info("{} cancel to like {}", username, post_id);
    }

    public void likeComment(String post_id, String comment_id){
        Post saved_post = postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        commentRepository.findById(comment_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_COMMENT);});

        CommentLike commentLike = CommentLike.builder()
                        .postId(post_id)
                .build();

        commentLikeRepository.save(commentLike);
    }

    public void cancelCommentLike(String post_id, String comment_id){
        Post saved_post = postRepository.findById(post_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        commentRepository.findById(comment_id)
                .orElseThrow(() -> {throw new CustomException(NOT_FOUND_COMMENT);});

        commentLikeRepository.deleteById(comment_id);
    }
}

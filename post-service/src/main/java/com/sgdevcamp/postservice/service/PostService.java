package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.dto.request.PostRequest;
import com.sgdevcamp.postservice.dto.request.PostUpdateRequest;
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
    private final HashtagService hashtagService;
    private final PostEventSender postEventSender;

    public PostResponse createPost(PostRequest postRequest){

        Post post = postRequest.toEntity();

        post = postRepository.save(post);
        postEventSender.sendPostCreated(post);

        log.info("post {} is saved successfully for user {}", post.getId(), post.getUsername());

        PostResponse postResponse = post.toResponse();

        return postResponse;
    }

    public boolean isExistPost(String id){
        return postRepository.existsById(id);
    }

    public boolean isUpdateHashtag(Post post, PostUpdateRequest postUpdateRequest){

        if(!postUpdateRequest.getHashTags().equals(post.getHashTags())) return true;

        return false;
    }

    public List<PostResponse> postsByUsername(String username){

        return postRepository.findByUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(Post::toResponse)
                .collect(Collectors.toList());
    }

    public Post getPostById(String id){

        return postRepository.findById(id).orElseThrow(() ->
                new CustomException(NOT_FOUND_POST));
    }

    public List<PostResponse> postsByIdIn(List<String> ids){

        return postRepository.findByIdInOrderByCreatedAtDesc(ids)
                .stream()
                .map(Post::toResponse)
                .collect(Collectors.toList());
    }

    public List<PostResponse> postsByUserIdIn(List<String> ids){

        return postRepository.findByUserIdInOrderByCreatedAtDesc(ids)
                .stream()
                .map(Post::toResponse)
                .collect(Collectors.toList());
    }

    public Post updatePost(String post_id, PostUpdateRequest postUpdateRequest){

        Post post = postRepository.findById(post_id).orElseThrow(() -> {throw new CustomException(NOT_FOUND_POST);});

        if(isUpdateHashtag(post, postUpdateRequest)){
            hashtagService.deleteHashtags(post_id);
            hashtagService.createHashtags(postUpdateRequest.getHashTags(), post);
        }

        post.update(postUpdateRequest);
        Post save_post = postRepository.save(post);

        log.info("successfully updated post {}", post_id);

        return save_post;
    }

    public void deletePost(String post_id, String username){

        postRepository
                .findById(post_id)
                .map(post ->{
                    if(!post.getUsername().equals(username)) throw new CustomException(NOT_ALLOWED_USER);
                    postRepository.delete(post);
                    log.info("deleting post {}", post_id);
                    return post;
                }).orElseThrow(() -> {
                    throw new CustomException(NOT_FOUND_POST);
                });
    }
}

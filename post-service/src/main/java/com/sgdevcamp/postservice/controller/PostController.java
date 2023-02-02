package com.sgdevcamp.postservice.controller;

import com.sgdevcamp.postservice.dto.request.CommentUpdateRequest;
import com.sgdevcamp.postservice.dto.request.PostRequest;
import com.sgdevcamp.postservice.dto.response.CommonResponse;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.service.PostService;
import com.sgdevcamp.postservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.NOT_ALLOWED_USER;

@Slf4j
@RestController
@RequestMapping("/post-server")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ResponseService responseService;

    @PostMapping("/posts")
    public CommonResponse createPost(@RequestBody PostRequest postRequest){
        log.info("received a request to create a post for image {}", postRequest);

        return responseService.getDataResponse(postService.createPost(postRequest));
    }

    @DeleteMapping("/posts/{id}")
    public CommonResponse deletePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal principal) {
        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);

        log.info("received a delete request for post id {} from user {}", id, principal.getName());

        postService.deletePost(id, principal.getName());

        return responseService.getSuccessResponse();
    }

    @GetMapping("/posts/me")
    public CommonResponse findCurrentUserPosts(@AuthenticationPrincipal Principal principal) {
        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);

        log.info("retrieving posts for user {}", principal.getName());

        List<PostResponse> posts = postService.postsByUsername(principal.getName());

        log.info("found {} posts for user {}", posts.size(), principal.getName());

        return responseService.getDataResponse(posts);
    }

    @GetMapping("/posts/{username}")
    public CommonResponse findUserPosts(@PathVariable("username") String username) {
        log.info("retrieving posts for user {}", username);

        List<PostResponse> posts = postService.postsByUsername(username);

        log.info("found {} posts for user {}", posts.size(), username);

        return responseService.getDataResponse(posts);
    }

    @PostMapping("/posts/in")
    public CommonResponse findPostsByIdIn(@RequestBody List<String> ids) {
        log.info("retrieving posts for {} ids", ids.size());

        List<PostResponse> posts = postService.postsByIdIn(ids);

        log.info("found {} posts", posts.size());

        return responseService.getDataResponse(posts);
    }

    @PostMapping("/posts/{postId}/comments")
    public CommonResponse createComment(@PathVariable(value = "postId") String post_id,
                                        @RequestBody CommentUpdateRequest commentUpdateRequest) {
        return responseService.getDataResponse(postService.createComment(post_id, commentUpdateRequest));
    }

    @GetMapping("/posts/{postId}/comments")
    public CommonResponse getComments(@PathVariable(value = "postId") String post_id) {
        return responseService.getDataResponse(postService.findAllComment(post_id));
    }

    @PatchMapping("/posts/{postId}/comments")
    public CommonResponse updateComment(@PathVariable(value = "postId") String post_id,
                                        @RequestBody CommentUpdateRequest commentUpdateRequest) {
        postService.updateComment(post_id, commentUpdateRequest);

        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public CommonResponse deleteComment(@PathVariable(value = "postId") String post_id,
                                      @PathVariable(value = "commentId") String comment_id) {
        postService.deleteComment(comment_id, post_id);

        log.info("post {} is successfully deleted", post_id);

        return responseService.getSuccessResponse();
    }

    @PostMapping("/posts/{postId}/like")
    public CommonResponse likePost(@PathVariable(value = "postId") String post_id){
        postService.likePost(post_id, "b");
        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/posts/{postId}/like")
    public CommonResponse cancelPostLike(@PathVariable(value = "postId") String post_id){
        postService.cancelPostLike(post_id, "b");
        return responseService.getSuccessResponse();
    }
}

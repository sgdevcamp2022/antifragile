package com.sgdevcamp.postservice.controller;

import com.sgdevcamp.postservice.dto.request.CommentCreateRequest;
import com.sgdevcamp.postservice.dto.request.CommentUpdateRequest;
import com.sgdevcamp.postservice.dto.request.PostRequest;
import com.sgdevcamp.postservice.dto.request.PostUpdateRequest;
import com.sgdevcamp.postservice.dto.response.CommonResponse;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.Image;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.*;

@Slf4j
@RestController
@RequestMapping("/post-server")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final HashtagService hashtagService;
    private final UploadService uploadService;
    private final ProfileService profileService;
    private final ResponseService responseService;

    @PostMapping("/posts")
    public CommonResponse createPost(@RequestBody PostRequest postRequest,
                                     @RequestPart List<MultipartFile> multipartFiles,
                                     @AuthenticationPrincipal Principal principal) throws IOException {

        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);

        log.info("received a request to create a post for image {}", postRequest);

        List<Image> saved_image = uploadService.save(multipartFiles, principal.getName());
        postRequest.setImages(saved_image);

        return responseService.getDataResponse(postService.createPost(postRequest));
    }

    @PutMapping("/posts/{id}")
    public CommonResponse updatePost(@PathVariable("id") String post_id,
                                     @RequestBody PostUpdateRequest postUpdateRequest,
                                     @AuthenticationPrincipal Principal principal){

        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);
        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);

        log.info("received a request to update a post {}", post_id);

        postService.updatePost(post_id, postUpdateRequest);

        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/posts/{id}")
    public CommonResponse deletePost(@PathVariable("id") String post_id, @AuthenticationPrincipal Principal principal) {

        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);
        Post post = postService.getPostById(post_id);

        log.info("received a delete request for post id {} from user {}", post_id, principal.getName());

        uploadService.delete(post.getImages());
        postService.deletePost(post_id, principal.getName());
        postLikeService.deleteAllPostLike(post_id);
        commentService.deleteAllByPostId(post_id);
        commentLikeService.cancelAllCommentLike(post_id);
        hashtagService.deleteHashtags(post_id);

        return responseService.getSuccessResponse();
    }

    @GetMapping("/profile/{username}")
    public CommonResponse getUserProfile(String username){
        return responseService.getDataResponse(profileService.getUserProfile(username));
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
                                        @RequestBody CommentCreateRequest commentCreateRequest) {

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);

        return responseService.getDataResponse(commentService.createComment(commentCreateRequest));
    }

    @GetMapping("/posts/{postId}/comments")
    public CommonResponse getComments(@PathVariable(value = "postId") String post_id) {

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);

        return responseService.getDataResponse(commentService.findAllComment(post_id));
    }

    @PatchMapping("/posts/{postId}/comments")
    public CommonResponse updateComment(@PathVariable(value = "postId") String post_id,
                                        @RequestBody CommentUpdateRequest commentUpdateRequest) {

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);
        commentService.updateComment(commentUpdateRequest);

        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public CommonResponse deleteComment(@PathVariable(value = "postId") String post_id,
                                      @PathVariable(value = "commentId") String comment_id) {

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);
        commentService.deleteComment(comment_id);

        log.info("post {} is successfully deleted", post_id);

        return responseService.getSuccessResponse();
    }

    @PostMapping("/posts/{postId}/like")
    public CommonResponse likePost(@AuthenticationPrincipal Principal principal,
                                   @PathVariable(value = "postId") String post_id){

        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);

        String like_id = postLikeService.likePost(post_id, principal.getName());

        return responseService.getDataResponse(like_id);
    }

    @DeleteMapping("/posts/{postId}/like/{likeId}")
    public CommonResponse cancelPostLike(@AuthenticationPrincipal Principal principal,
                                         @PathVariable(value = "postId") String post_id,
                                         @PathVariable(value = "likeId") String like_id){

        if(principal == null) throw new CustomException(NOT_ALLOWED_USER);

        postLikeService.deletePostLike(like_id);

        log.info("user {} delete to like post {}", principal.getName(), post_id);

        return responseService.getSuccessResponse();
    }

    @PostMapping("/posts/{postId}/like/comment/{commentId}")
    public CommonResponse likeComment(@PathVariable(value = "postId") String post_id,
                                      @PathVariable(value = "commentId") String comment_id){

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);
        if(!commentService.isExistComment(comment_id)) throw new CustomException(NOT_FOUND_COMMENT);

        commentLikeService.likeComment(post_id, comment_id);

        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/posts/{postId}/like/comment/{commentId}")
    public CommonResponse cancelCommentLike(@PathVariable(value = "postId") String post_id,
                                            @PathVariable(value = "commentId") String comment_id){

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);
        if(!commentService.isExistComment(comment_id)) throw new CustomException(NOT_FOUND_COMMENT);

        commentLikeService.cancelCommentLike(post_id, comment_id);

        return responseService.getSuccessResponse();
    }
}

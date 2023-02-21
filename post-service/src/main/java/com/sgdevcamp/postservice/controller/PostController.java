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

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @PostMapping(value = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CommonResponse createPost(PostRequest postRequest,
                                     @RequestPart("multipartFiles") List<MultipartFile> multipartFiles) throws IOException {

        log.info("received a request to create a post for image {}", postRequest);

        List<Image> saved_image = uploadService.save(multipartFiles, postRequest.getUsername());
        postRequest.setImages(saved_image);

        return responseService.getDataResponse(postService.createPost(postRequest));
    }

    @PutMapping("/posts/{id}")
    public CommonResponse updatePost(@PathVariable("id") String post_id,
                                     @RequestBody PostUpdateRequest postUpdateRequest){

        if(!postService.isExistPost(post_id)) throw new CustomException(NOT_FOUND_POST);

        log.info("received a request to update a post {}", post_id);

        postService.updatePost(post_id, postUpdateRequest);

        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/posts/{id}")
    public CommonResponse deletePost(@PathVariable("id") String post_id, @RequestParam("username") String username) {

        Post post = postService.getPostById(post_id);

        log.info("received a delete request for post id {} from user {}", post_id, username);

        uploadService.delete(post.getImages());
        postService.deletePost(post_id, username);
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

        return responseService.getDataResponse(commentService.createComment(commentCreateRequest, post_id));
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
    public CommonResponse likePost(@PathVariable(value = "postId") String post_id,
                                   @RequestBody String username){

        String like_id = postLikeService.likePost(post_id, username);

        return responseService.getDataResponse(like_id);
    }

    @DeleteMapping("/posts/{postId}/like/{likeId}")
    public CommonResponse cancelPostLike(@PathVariable(value = "postId") String post_id,
                                         @PathVariable(value = "likeId") String like_id){

        postLikeService.deletePostLike(like_id);

        log.info("user delete to like post {}", post_id);

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
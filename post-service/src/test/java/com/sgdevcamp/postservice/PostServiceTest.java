package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.dto.request.PostRequest;
import com.sgdevcamp.postservice.dto.request.PostUpdateRequest;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.messaging.PostEventSender;
import com.sgdevcamp.postservice.model.Image;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.repository.*;
import com.sgdevcamp.postservice.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostEventSender postEventSender;

    final String username = "user1";

    private static Post post;

    private static PostRequest postRequest;

    @BeforeEach
    public void beforeEach(){
        Image image = new Image();
        image.setPath("test.jpg");
        List<Image> images = new LinkedList<>();
        images.add(image);

        postRequest = new PostRequest();
        postRequest.setUsername(username);
        postRequest.setImages(images);
        postRequest.setContent("좋아요 눌러주세요");
        postRequest.setHashTags(new ArrayList<>());

        post = postRequest.toEntity();
    }

    @Test
    @DisplayName("게시글 생성")
    public void createPost() {
        // given
        when(postRepository.save(post)).thenReturn(post);

        // when
        PostResponse postResponse = postService.createPost(postRequest);

        // then
        assertEquals(postResponse.getUsername(), postRequest.getUsername());
        assertEquals(postResponse.getContent(), postRequest.getContent());
    }

    @Test
    @DisplayName("주어진 고유 식별자 id를 가진 게시글의 존재 여부")
    public void isExistPost() {
        // given
        when(postRepository.existsById(any())).thenReturn(true);

        // then
        assertEquals(postService.isExistPost("1"), true);
    }

    @Test
    @DisplayName("고유 식별자로 게시글 조회")
    public void getPostById() {
        // given
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        // when
        Post post = postService.getPostById("1");

        // then
        assertThat(post).isNotNull();
    }

    @Test
    @DisplayName("아이디로 게시글 조회")
    public void getPostsByUsername() {
        // given
        List<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByUsernameOrderByCreatedAtDesc(any())).thenReturn(postList);

        // when
        List<PostResponse> postResponse = postService.postsByUsername(username);

        // then
        assertEquals(postResponse.size(), 1);
    }

    @Test
    @DisplayName("내림차순 생성일로 게시글 id들을 이용해 게시글 조회")
    public void getPostsByIdIn() {
        // given
        List<Post> postList = new ArrayList<>();
        postList.add(post);
        when(postRepository.findByIdInOrderByCreatedAtDesc(any())).thenReturn(postList);

        // when
        List<PostResponse> postResponse = postService.postsByIdIn(any());

        // then
        assertEquals(postResponse.size(), 1);
    }

    @Test
    @DisplayName("게시글 수정")
    public void updatePost() {
        // given
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest();
        postUpdateRequest.setHashTags(post.getHashTags());

        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);

        // when
        Post updatePost = postService.updatePost(any(), postUpdateRequest);

        // then
        assertThat(updatePost).isNotNull();
    }

}
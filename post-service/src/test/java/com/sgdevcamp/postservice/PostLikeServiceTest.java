package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.model.PostLike;
import com.sgdevcamp.postservice.repository.PostLikeRepository;
import com.sgdevcamp.postservice.service.PostLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostLikeServiceTest {

    @InjectMocks
    private PostLikeService postLikeService;

    @Mock
    private PostLikeRepository postLikeRepository;

    private static PostLike postLike;

    private static final String post_id = "1";

    private static final String username = "userA";

    @BeforeEach
    public void beforeEach(){

        postLike = PostLike.builder()
                .id("1")
                .postId(post_id)
                .username(username)
                .build();
    }

    @Test
    @DisplayName("게시글 좋아요")
    public void createPostLike() {
        // given
        when(postLikeRepository.save(any())).thenReturn(postLike);

        // when
        String like_id = postLikeService.likePost(post_id, username);

        // then
        assertEquals(like_id, postLike.getId());
    }

    @Test
    @DisplayName("게시글 좋아요 카운트")
    public void countLikePost() {
        // given
        List<PostLike> postLikes = new LinkedList<>();
        postLikes.add(postLike);

        when(postLikeRepository.findByPostId(post_id)).thenReturn(postLikes);
        when(postLikeRepository.countByPostId(post_id)).thenReturn(postLikes.stream().count());

        // when
        Long count_like = postLikeService.countLikePost(post_id);

        // then
        assertEquals(count_like, postLikes.size());
    }

}

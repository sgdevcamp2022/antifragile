package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.model.CommentLike;
import com.sgdevcamp.postservice.repository.CommentLikeRepository;
import com.sgdevcamp.postservice.service.CommentLikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentLikeServiceTest {

    @Mock
    private CommentLikeRepository commentLikeRepository;

    @InjectMocks
    private CommentLikeService commentLikeService;

    private static CommentLike commentLike;

    private static final String id = "1";

    private static final String commentId = "1";

    private static final String postId = "1";

    private static final String username = "userA";

    @BeforeEach
    public void beforeEach(){

        commentLike = CommentLike.builder()
                .id(id)
                .commentId(commentId)
                .postId(postId)
                .username(username)
                .build();

    }

    @Test
    @DisplayName("댓글 좋아요")
    public void likeComment() {
        // given
        when(commentLikeRepository.save(any())).thenReturn(commentLike);

        // when
        String comment_id = commentLikeService.likeComment(postId, commentId);

        // then
        assertEquals(comment_id, commentId);
    }
}

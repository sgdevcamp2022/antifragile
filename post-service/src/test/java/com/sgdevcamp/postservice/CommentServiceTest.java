package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.dto.request.CommentCreateRequest;
import com.sgdevcamp.postservice.dto.request.CommentUpdateRequest;
import com.sgdevcamp.postservice.dto.response.CommentResponse;
import com.sgdevcamp.postservice.model.Comment;
import com.sgdevcamp.postservice.repository.CommentRepository;
import com.sgdevcamp.postservice.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    private static CommentCreateRequest commentCreateRequest;

    private static CommentUpdateRequest commentUpdateRequest;

    private static Comment comment;

    private static String post_id;

    @BeforeEach
    public void beforeEach(){
        post_id = "1";

        commentCreateRequest = new CommentCreateRequest();
        commentCreateRequest.setUsername("user1");
        commentCreateRequest.setContent("댓글 달고 갑니다.");

        commentUpdateRequest = new CommentUpdateRequest();
        commentUpdateRequest.setUsername("user1");
        commentUpdateRequest.setContent("댓글 달고 갑니다.");

        comment = Comment.builder()
                .id("1")
                .username("user1")
                .content("댓글 달고 갈게요~")
                .postId("1")
                .build();
    }

    @Test
    @DisplayName("댓글 생성")
    public void createComment() {
        // given
        when(commentRepository.save(any())).thenReturn(comment);

        // when
        CommentResponse commentResponse = commentService.createComment(commentCreateRequest, post_id);

        // then
        assertEquals(commentResponse.getUsername(), commentCreateRequest.getUsername());
    }

    @Test
    @DisplayName("모든 댓글 조회")
    public void findAllComment() {
        // given
        List<Comment> commentList = new LinkedList<>();
        commentList.add(comment);

        when(commentRepository.findAllByPostId(any())).thenReturn(commentList);

        // when
        List<CommentResponse> commentResponse = commentService.findAllComment(post_id);

        // then
        assertEquals(commentResponse.size(), 1);
    }

    @Test
    @DisplayName("존재하는 댓글인지")
    public void isExistComment() {
        // given
        when(commentRepository.existsById(any())).thenReturn(true);

        // when
        boolean flag = commentService.isExistComment(any());

        // then
        assertEquals(flag, true);
    }

    @Test
    @DisplayName("댓글 수정")
    public void updateComment() {
        // given
        when(commentRepository.save(any())).thenReturn(comment);
        when(commentRepository.findById(any())).thenReturn(Optional.of(comment));

        // when
        CommentResponse commentResponse = commentService.updateComment(commentUpdateRequest);

        // then
        assertEquals(commentResponse.getUsername(), commentUpdateRequest.getUsername());
    }
}

package com.sgdevcamp.postservice.feed;

import com.sgdevcamp.postservice.dto.follow.response.PagedResult;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.repository.PostRepository;
import com.sgdevcamp.postservice.service.feed.FeedService;
import com.sgdevcamp.postservice.service.follow.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    public void beforeEach(){

    }

    @Test
    @DisplayName("팔로잉 유저가 올린 최신 피드 가져오기")
    public void getUserFeed(){
        // given
        int page = 20;
        String user_id = "1";

        PagedResult<User> followings = new PagedResult<>();
        List<User> content = new ArrayList<>();
        content.add(User.builder().userId("1").build());
        followings.setContent(content);
        followings.setPage(20);

        List<Post> posts = new ArrayList<>();

        Post post = Post.builder()
                .content("hi")
                .images(new ArrayList<>())
                .build();

        // when
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(userService.findPaginatedFollowings(user_id, 0, page)).thenReturn(followings);
        when(postRepository.findByCreatedAtGreaterThanAndUserIdInOrderByCreatedAtDesc(any(), any())).thenReturn(posts);

        // then
        assertThat(feedService.getUserFeed(user_id, "1", Optional.of("2"))).isNotNull();
    }
}

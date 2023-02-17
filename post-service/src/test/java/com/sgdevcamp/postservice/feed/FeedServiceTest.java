package com.sgdevcamp.postservice.feed;

import com.sgdevcamp.postservice.model.feed.UserFeed;
import com.sgdevcamp.postservice.repository.feed.FeedRepository;
import com.sgdevcamp.postservice.service.PostService;
import com.sgdevcamp.postservice.service.feed.FeedService;
import com.sgdevcamp.postservice.service.follow.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.nio.ByteBuffer;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedServiceTest {

    @InjectMocks
    private FeedService feedService;

    @Mock
    private UserService userService;

    @Mock
    private PostService postService;

    @Mock
    private FeedRepository feedRepository;

    private UserFeed userFeed;

    @BeforeEach
    public void beforeEach(){

        userFeed = UserFeed.builder()
                .username("userA")
                .build();
    }

    @Test
    @DisplayName("피드 가져오기")
    public void getUserFeed(){
        // given
        int pageSize = 10;
        CassandraPageRequest pageRequest = CassandraPageRequest.first(pageSize);
        Optional<String> pagingStateOptional = Optional.ofNullable(pageRequest.getPagingState())
                .map(ByteBuffer::array)
                .map(String::new);

        List<UserFeed> userFeeds =  new ArrayList<>();
        userFeeds.add(userFeed);

        Slice<UserFeed> page = new SliceImpl<>(userFeeds);
        when(feedRepository.findByUsername(any(), any())).thenReturn(page);

        // when, then
        assertThat(feedService.getUserFeed(userFeed.getUsername(), pagingStateOptional)).isNotNull();
    }
}

package com.example.feedservice.service;

import com.datastax.oss.driver.api.core.cql.PagingState;
import com.example.feedservice.exception.CustomException;
import com.example.feedservice.exception.CustomExceptionStatus;
import com.example.feedservice.model.Post;
import com.example.feedservice.model.UserFeed;
import com.example.feedservice.payload.SlicedResult;
import com.example.feedservice.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedRepository feedRepository;
    private final PostService postService;
    private final AuthService authService;
    private final static int PAGE_SIZE = 20;

    public SlicedResult<Post> getUserFeed(String username, Optional<String> pagingState) {

        log.info("getting feed for user {} isFirstPage {}", username, pagingState.isEmpty());

        CassandraPageRequest request = pagingState
                .map(pState -> CassandraPageRequest
                        .of(PageRequest.of(0, PAGE_SIZE), ByteBuffer.wrap(PagingState.fromString(pState).toString().getBytes())))
                .orElse(CassandraPageRequest.first(PAGE_SIZE));

        Slice<UserFeed> page =
                feedRepository.findByUsername(username, request);

        if(page.isEmpty()) {
            throw new CustomException(CustomExceptionStatus.UNABLE_TO_GET_FEED);
        }

        String pageState = null;

        if(!page.isLast()) {
            pageState = ((CassandraPageRequest)page.getPageable())
                    .getPagingState().toString();
        }

        List<Post> posts = getPosts(page);

        return SlicedResult
                .<Post>builder()
                .content(posts)
                .isLast(page.isLast())
                .pagingStats(pageState)
                .build();
    }

    private List<Post> getPosts(Slice<UserFeed> page) {

        String token = authService.getAccessToken();

        List<String> postIds = page.stream()
                .map(feed -> feed.getPostId())
                .collect(toList());

        List<Post> posts = postService.findPostsIn(token, postIds);

        List<String> usernames = posts.stream()
                .map(Post::getUsername)
                .distinct()
                .collect(toList());

        Map<String, String> usersProfilePics =
                authService.usersProfilePic(token, new ArrayList<>(usernames));

        posts.forEach(post -> post.setUserProfilePic(
                usersProfilePics.get(post.getUsername())));

        return posts;
    }
}

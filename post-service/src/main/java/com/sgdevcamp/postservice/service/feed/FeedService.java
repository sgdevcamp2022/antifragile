package com.sgdevcamp.postservice.service.feed;

import com.datastax.oss.driver.api.core.cql.PagingState;
import com.sgdevcamp.postservice.dto.feed.SlicedResult;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.exception.CustomExceptionStatus;
import com.sgdevcamp.postservice.model.feed.UserFeed;
import com.sgdevcamp.postservice.repository.feed.FeedRepository;
import com.sgdevcamp.postservice.service.PostService;
import com.sgdevcamp.postservice.service.follow.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserService userService;
    private final PostService postService;
    private final static int PAGE_SIZE = 20;

    public SlicedResult<PostResponse> getUserFeed(String username, Optional<String> pagingState) {

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

        List<PostResponse> posts = getPosts(page);

        return SlicedResult
                .<PostResponse>builder()
                .content(posts)
                .isLast(page.isLast())
                .pagingStats(pageState)
                .build();
    }

    private List<PostResponse> getPosts(Slice<UserFeed> page) {

        List<String> postIds = page.stream()
                .map(feed -> feed.getPostId())
                .collect(toList());

        List<PostResponse> posts = postService.postsByIdIn(postIds);

        List<String> usernames = posts.stream()
                .map(PostResponse::getUsername)
                .distinct()
                .collect(toList());

        Map<String, String> usersProfilePics =
                userService.usersProfilePic(usernames);

        posts.forEach(post -> post.setUserProfilePic(
                usersProfilePics.get(post.getUsername())));

        return posts;
    }

}

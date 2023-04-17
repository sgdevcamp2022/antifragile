package com.sgdevcamp.postservice.service.feed;

import com.sgdevcamp.postservice.client.MembershipClient;
import com.sgdevcamp.postservice.dto.feed.SlicedResult;
import com.sgdevcamp.postservice.dto.follow.response.PagedResult;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.model.Post;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.repository.PostRepository;
import com.sgdevcamp.postservice.service.follow.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.examples.lib.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final MembershipClient membershipClient;
    private final static int PAGE_SIZE = 20;

    public SlicedResult<PostResponse> getUserFeed(String user_id, String last_post_id, Optional<String> pagingState) {

        log.info("getting feed for user {} isFirstPage {}", user_id, pagingState.isEmpty());

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

        Instant last_post_created_at = postRepository.findById(last_post_id)
                .map(Post::getCreatedAt)
                .orElse(null);

        PagedResult<User> followings = userService.findPaginatedFollowings(user_id, 0, PAGE_SIZE);

        List<String> user_ids = followings.getContent().stream().map(User::getUserId).collect(toList());

        List<Post> posts = postRepository.findByCreatedAtGreaterThanAndUserIdInOrderByCreatedAtDesc(last_post_created_at, user_ids);

        List<PostResponse> userFeeds = posts.stream()
                .map(post -> {
                    Account account = membershipClient.findProfile(Long.valueOf(post.getUserId()));

                    return PostResponse.builder()
                            .id(post.getId())
                            .userId(post.getUserId())
                            .username(account.getUsername())
                            .userProfilePic(account.getProfileImageUrl())
                            .imageUrl(post.getImages().stream().map(i -> i.getPath()).collect(Collectors.toList()))
                            .content(post.getContent())
                            .hashTags(post.getHashTags())
                            .createdAt(post.getCreatedAt())
                            .build();
                })
                .collect(toList());

        Slice<PostResponse> sliced_page = checkLastPageForPost(pageable, userFeeds);

        return SlicedResult
                .<PostResponse>builder()
                .content(sliced_page.getContent())
                .isLast(sliced_page.isLast())
                .pagingStats(sliced_page.getPageable().toString())
                .build();
    }

    private  Slice<PostResponse> checkLastPageForPost(Pageable pageable, List<PostResponse> results){

        boolean hasNext = false;

        if(results.size()> pageable.getPageSize()){
            hasNext=true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}

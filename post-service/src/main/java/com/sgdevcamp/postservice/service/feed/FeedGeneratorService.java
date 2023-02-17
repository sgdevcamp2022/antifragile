package com.sgdevcamp.postservice.service.feed;

import com.sgdevcamp.postservice.dto.follow.response.PagedResult;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.exception.CustomExceptionStatus;
import com.sgdevcamp.postservice.model.feed.UserFeed;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.repository.feed.FeedRepository;
import com.sgdevcamp.postservice.service.PostService;
import com.sgdevcamp.postservice.service.follow.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedGeneratorService {

    private final FeedRepository feedRepository;
    private final PostService postService;
    private final UserService userService;
    private final static int PAGE_SIZE = 20;

    public void addToFeed(String username){

        log.info("adding post to feed");

        boolean isLast = false;
        int page = 0;
        int size = PAGE_SIZE;

        while(!isLast){

            User me = userService.findUser(username);
            PagedResult<User> followers = userService.findPaginatedFollowers(username, page, size);

            if(followers == null){

                String message = String.format("unable to get followers for user %s", username);

                log.warn(message);
                throw new CustomException(CustomExceptionStatus.UNABLE_TO_GET_FOLLOWERS);
            }

            log.info("found {} followers for user {}, page {}",
                    followers.getTotalElements(), username, page);

            List<PostResponse> posts = postService.postsByIdIn(followers.getContent().stream()
                    .map(User::getId)
                    .map(String::valueOf)
                    .collect(Collectors.toList()));

            posts.stream()
                    .map(p -> convertTo(me, p))
                    .forEach(feedRepository::insert);

            isLast = followers.isLast();
            page++;
        }
    }

    private UserFeed convertTo(User user, PostResponse post){

        return UserFeed
                .builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}

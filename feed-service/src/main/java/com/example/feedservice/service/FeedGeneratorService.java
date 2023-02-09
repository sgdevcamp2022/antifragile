package com.example.feedservice.service;

import com.example.feedservice.client.FollowServiceClient;
import com.example.feedservice.config.JwtConfig;
import com.example.feedservice.exception.CustomException;
import com.example.feedservice.exception.CustomExceptionStatus;
import com.example.feedservice.model.Post;
import com.example.feedservice.model.User;
import com.example.feedservice.model.UserFeed;
import com.example.feedservice.payload.PagedResult;
import com.example.feedservice.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedGeneratorService {
    private final JwtConfig jwtConfig;
    private final AuthService tokenService;
    private final FeedRepository feedRepository;
    private final FollowServiceClient followServiceClient;
    private final static int PAGE_SIZE = 20;

    public void addToFeed(Post post){
        log.info("adding post {} to feed for user {}", post.getUsername(), post.getId());

        String token = tokenService.getAccessToken();

        boolean isLast = false;
        int page = 0;
        int size = PAGE_SIZE;

        while(!isLast){
            ResponseEntity<PagedResult<User>> response = followServiceClient.findFollowers(jwtConfig.getPrefix() + token,
                    post.getUsername(), page, size);

            if(response.getStatusCode().is2xxSuccessful()){
                PagedResult<User> result = response.getBody();

                log.info("found {} followers for user {}, page {}",
                        result.getTotalElements(), post.getUsername(), page);

                result.getContent()
                        .stream()
                        .map(user -> convertTo(user, post))
                        .forEach(feedRepository::insert);

                isLast = result.isLast();
                page++;
            }else{
                String message = String.format("unable to get followers for user %s", post.getUsername());

                log.warn(message);
                throw new CustomException(CustomExceptionStatus.UNABLE_TO_GET_FOLLOWERS);
            }
        }
    }

    private UserFeed convertTo(User user, Post post){
        return UserFeed
                .builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}

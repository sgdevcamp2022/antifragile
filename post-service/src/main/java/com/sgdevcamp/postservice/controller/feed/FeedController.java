package com.sgdevcamp.postservice.controller.feed;

import com.sgdevcamp.postservice.dto.feed.SlicedResult;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.service.feed.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/feed-server")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/feed/{userId}")
    public ResponseEntity<SlicedResult<PostResponse>> getFeed(
            @PathVariable("userId") String user_id, @RequestParam String last_post_id,
            @RequestParam(value = "ps",required = false) Optional<String> pagingState) {

        log.info("fetching feed for user {} isFirstPage {}", user_id, pagingState.isEmpty());

        return ResponseEntity.ok(feedService.getUserFeed(user_id, last_post_id, pagingState));
    }
}

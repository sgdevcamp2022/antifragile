package com.sgdevcamp.postservice.controller.feed;

import com.sgdevcamp.postservice.dto.feed.SlicedResult;
import com.sgdevcamp.postservice.dto.response.PostResponse;
import com.sgdevcamp.postservice.service.feed.FeedGeneratorService;
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
    private final FeedGeneratorService feedGeneratorService;

    @PostMapping("/feed/{username}")
    public ResponseEntity<SlicedResult<PostResponse>> getFeed(
            @PathVariable String username,
            @RequestParam(value = "ps",required = false) Optional<String> pagingState) {

        log.info("fetching feed for user {} isFirstPage {}",
                username, pagingState.isEmpty());

        feedGeneratorService.addToFeed(username);

        return ResponseEntity.ok(feedService.getUserFeed(username, pagingState));
    }
}

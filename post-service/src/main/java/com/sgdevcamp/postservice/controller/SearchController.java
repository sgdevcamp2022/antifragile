package com.sgdevcamp.postservice.controller;

import com.sgdevcamp.postservice.dto.response.CommonResponse;
import com.sgdevcamp.postservice.dto.response.HashtagResponse;
import com.sgdevcamp.postservice.service.HashtagService;
import com.sgdevcamp.postservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/search-server")
@RequiredArgsConstructor
public class SearchController {

    private final HashtagService hashtagService;
    private final ResponseService responseService;

    @GetMapping("/search/hashtag/{tag}")
    public CommonResponse searchHashtag(@PathVariable String tag,
                                           @PageableDefault(size = 50) Pageable pageable){

        Page<HashtagResponse> hashtags = hashtagService.searchHashtag(tag, pageable);

        return responseService.getDataResponse(hashtags);
    }
}

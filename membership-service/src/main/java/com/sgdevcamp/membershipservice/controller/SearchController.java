package com.sgdevcamp.membershipservice.controller;

import com.sgdevcamp.membershipservice.dto.response.CommonResponse;
import com.sgdevcamp.membershipservice.dto.response.UserSummary;
import com.sgdevcamp.membershipservice.service.ResponseService;
import com.sgdevcamp.membershipservice.service.SearchService;
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

    private final SearchService searchService;
    private final ResponseService responseService;

    @GetMapping("/search/user/{username}")
    public CommonResponse searchUser(@PathVariable String username,
                                        @PageableDefault(size = 50) Pageable pageable){

        Page<UserSummary> users = searchService.searchUser(username, pageable);

        return responseService.getDataResponse(users);
    }
}

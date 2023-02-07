package com.example.feedservice.client;

import com.example.feedservice.model.User;
import com.example.feedservice.payload.PagedResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="follow-service")
public interface FollowServiceClient {

    @GetMapping("/follow-server/users/paginated/{username}/followers")
    ResponseEntity<PagedResult<User>> findFollowers(
            @RequestHeader("Authorization") String token,
            @PathVariable("username") String username,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "size") int size);
}

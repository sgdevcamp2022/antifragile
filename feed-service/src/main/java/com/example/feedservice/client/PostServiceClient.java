package com.example.feedservice.client;

import com.example.feedservice.model.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "post-service")
public interface PostServiceClient {

    @PostMapping("/post-server/posts/in")
    ResponseEntity<List<Post>> findPostsByIdIn(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> ids);
}

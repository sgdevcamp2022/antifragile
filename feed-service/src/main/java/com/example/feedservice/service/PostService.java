package com.example.feedservice.service;

import com.example.feedservice.client.PostServiceClient;
import com.example.feedservice.config.JwtConfig;
import com.example.feedservice.exception.CustomException;
import com.example.feedservice.model.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.feedservice.exception.CustomExceptionStatus.UNABLE_TO_GET_POST;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostServiceClient postServiceClient;
    private final JwtConfig jwtConfig;

    public List<Post> findPostsIn(String token, List<String> ids){
        log.info("finding posts for ids {}", ids);

        ResponseEntity<List<Post>> response =
                postServiceClient.findPostsByIdIn(jwtConfig.getPrefix() + token, ids);

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new CustomException(UNABLE_TO_GET_POST);
        }
    }

}

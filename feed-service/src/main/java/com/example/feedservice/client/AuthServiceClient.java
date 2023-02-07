package com.example.feedservice.client;

import com.example.feedservice.payload.JwtAuthenticationResponse;
import com.example.feedservice.payload.ServiceLoginRequest;
import com.example.feedservice.payload.UserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name="auth-service")
public interface AuthServiceClient {

    @PostMapping("/membership-server/login")
    ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody ServiceLoginRequest request);

    @PostMapping("/membership-server/auth/summary/in")
    ResponseEntity<List<UserSummary>> findByUsernameIn(
            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usernames);
}

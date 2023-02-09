package com.example.feedservice.service;

import com.example.feedservice.client.MembershipServiceClient;
import com.example.feedservice.config.JwtConfig;
import com.example.feedservice.exception.CustomException;
import com.example.feedservice.payload.JwtAuthenticationResponse;
import com.example.feedservice.payload.ServiceLoginRequest;
import com.example.feedservice.payload.UserSummary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.feedservice.exception.CustomExceptionStatus.INVALID_JWT;
import static com.example.feedservice.exception.CustomExceptionStatus.UNABLE_TO_GET_USER;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MembershipServiceClient authClient;
    private final ServiceLoginRequest serviceLoginRequest;
    private final JwtConfig jwtConfig;

    public String getAccessToken() {

        ResponseEntity<JwtAuthenticationResponse> response =
                authClient.signin(serviceLoginRequest);

        if(!response.getStatusCode().is2xxSuccessful()) {
            String message = String.format("unable to get access token for service account, %s",
                    response.getStatusCode());

            log.error(message);
            throw new CustomException(INVALID_JWT);
        }

        return response.getBody().getAccessToken();
    }

    public Map<String, String> usersProfilePic(String token,
                                               List<String> usernames) {

        ResponseEntity<List<UserSummary>> response =
                authClient.findByUsernameIn(
                        jwtConfig.getPrefix() + token, usernames);

        if(!response.getStatusCode().is2xxSuccessful()) {
            String message = String.format("unable to get user summaries %s",
                    response.getStatusCode());

            log.error(message);
            throw new CustomException(UNABLE_TO_GET_USER);
        }

        return response
                .getBody()
                .stream()
                .collect(toMap(UserSummary::getUsername,
                        UserSummary::getProfilePicture));
    }

}

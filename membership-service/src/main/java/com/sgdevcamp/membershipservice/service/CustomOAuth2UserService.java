package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final CookieUtil cookieUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        log.info("{}", oAuth2Attribute);

        User user = saveUser(oAuth2Attribute);

        var memberAttribute = oAuth2Attribute.convertToMap();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                memberAttribute, "email");
    }

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String user_email = String.valueOf(((DefaultOAuth2User) authentication.getPrincipal()).getAttributes().get("email"));

        User user = new User().builder()
                .username(user_email).build();

        String refresh_token = jwtUtil.generateRefreshToken(user);
        redisUtil.setDataExpire(refresh_token, user_email, JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);

        Cookie refresh_token_cookie = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refresh_token);

        response.setContentType(APPLICATION_JSON_VALUE);
        response.addCookie(refresh_token_cookie);

        String accessToken = jwtUtil.generateToken(user);
        Date expiredTime = jwtUtil.getExpiredTime(accessToken);

        response.sendRedirect("http://localhost:5555/auth?" +
                "accessToken="+accessToken+
                "&expiredTime="+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expiredTime));

    }

    @Transactional
    public User saveUser(OAuth2Attribute attribute){
        return userRepository.save(
                userRepository.findByEmail(attribute.getEmail())
                        .orElse(attribute.toEntity(attribute))
        );
    }

}

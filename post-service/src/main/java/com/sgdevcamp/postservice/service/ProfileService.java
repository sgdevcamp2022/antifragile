package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.dto.response.ProfileResponse;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.repository.PostRepository;
import com.sgdevcamp.postservice.repository.follow.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.USERNAME_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ProfileResponse getUserProfile(String username){

        User saved_user = userRepository.findByUsername(username).orElseThrow(()->{throw new CustomException(USERNAME_NOT_EXIST);});

        return ProfileResponse.builder()
                .username(username)
                .postCnt(postRepository.countByUsername(username))
                .profileName(saved_user.getName())
                .imageUrl(saved_user.getProfilePic())
                .followerCnt(userRepository.findInDegree(username))
                .followingCnt(userRepository.findOutDegree(username))
                .build();
    }
}

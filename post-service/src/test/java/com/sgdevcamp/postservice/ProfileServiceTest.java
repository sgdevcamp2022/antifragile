package com.sgdevcamp.postservice;

import com.sgdevcamp.postservice.dto.response.ProfileResponse;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.repository.PostRepository;
import com.sgdevcamp.postservice.repository.follow.UserRepository;
import com.sgdevcamp.postservice.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    private static User saved_user;

    @BeforeEach
    public void beforeEach(){
        saved_user = User
                .builder()
                .userId("1")
                .name("java")
                .username("test00")
                .profilePic("test.jpa")
                .build();
    }

    @Test
    @DisplayName("프로필 조회")
    public void getUserProfile() {
        // given
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(saved_user));

        // when
        ProfileResponse profileResponse = profileService.getUserProfile(saved_user.getUsername());

        // then
        assertThat(profileResponse).isNotNull();
    }
}

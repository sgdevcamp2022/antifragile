package com.sgdevcamp.membershipservice;

import com.sgdevcamp.membershipservice.dto.request.ProfileRequest;
import com.sgdevcamp.membershipservice.dto.request.UserDto;
import com.sgdevcamp.membershipservice.dto.response.MailResponse;
import com.sgdevcamp.membershipservice.messaging.UserEventSender;
import com.sgdevcamp.membershipservice.model.Salt;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.repository.UserRepository;
import com.sgdevcamp.membershipservice.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SaltUtil saltUtil;

    @Mock
    private RedisUtil redisUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserEventSender userEventSender;

    @Mock
    private EmailService emailService;

    private static User user;

    private static final String email = "test@test.com";

    private static final String username = "user00";

    private static final String password = "password";

    private static final String name = "chu";

    @BeforeEach
    public void beforeEach(){

        user = User.builder()
                .username("user00")
                .email("test@gmail.com")
                .password("password")
                .salt(new Salt("a"))
                .build();

    }

    @Test
    @DisplayName("?????? ??????")
    public void addUser() {
        // given
        UserDto userDto = UserDto.builder()
                .email(email)
                .username(username)
                .password(password)
                .name(name)
                .build();

        when(userRepository.save(any())).thenReturn(user);

        // when
        UserDto saved_user = userService.signUp(userDto);

        // then
        assertThat(saved_user.getUsername()).isEqualTo(userDto.getUsername());
    }

    @Test
    @DisplayName("???????????? ?????? ??????")
    public void findByUsername() {
        // given
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // when
        User saved_user = userService.findByUsername(username);

        // then
        assertThat(saved_user).isEqualTo(user);
    }

    @Test
    @DisplayName("????????? ???????????? ????????? ??????")
    public void findByUsernameIn() {
        // given
        List<String> usernames = new ArrayList<>();
        usernames.add(username);

        List<User> users = new LinkedList<>();
        users.add(user);

        when(userRepository.findByUsernameIn(any())).thenReturn(users);

        // when
        List<User> result = userService.findByUsernameIn(usernames);

        // then
        assertThat(result).isEqualTo(users);
    }

    @Test
    @DisplayName("?????? ??????")
    public void sendEmail() {
        // when
        MailResponse result = userService.sendEmail(email);

        // then
        assertThat(result.getCode()).isNotNull();
    }

    @Test
    @DisplayName("?????? ????????? ????????????")
    public void getMyProfile() {
        // when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // then
        assertThat(userService.getMyProfile(username)).isNotNull();
    }

    @Test
    @DisplayName("?????? ????????? ??????")
    public void modifyProfile() {
        // when
        ProfileRequest profileRequest = new ProfileRequest(username, "?????????~");
        when(userRepository.save(any())).thenReturn(user);

        // then
        assertThat(userService.modifyProfile(user, profileRequest)).isNotNull();
    }

    @Test
    @DisplayName("?????? ??????, ????????? ?????? ????????????")
    public void getNameAndPhoto() {
        // when
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // then
        assertThat(userService.getNameAndPhoto(1L)).isNotNull();
    }

    @Test
    @DisplayName("???????????? ?????? ?????? UUID??? ????????? ??????")
    public void isPasswordUuidValidate() {
        // when
        when(redisUtil.getData(any())).thenReturn("1");

        // then
        assertTrue(userService.isPasswordUuidValidate(any()));
    }

    @Test
    @DisplayName("??????????????? ??????????")
    public void isPasswordEqual() {
        // when
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(saltUtil.encodePassword(user.getSalt().getSalt(), password)).thenReturn(password);

        // then
        assertTrue(userService.isPasswordEqual(username, password));
    }
}

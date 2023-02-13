package com.sgdevcamp.postservice.follow;

import com.sgdevcamp.postservice.model.follow.NodeDegree;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.repository.follow.UserRepository;
import com.sgdevcamp.postservice.service.follow.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private static User save_userA;

    private static User save_userB;

    private static final String usernameA = "userA";

    private static final String usernameB = "userB";

    @BeforeEach
    public void beforeEach(){

        save_userA = User.builder()
                .id(1L)
                .userId("1")
                .username(usernameA)
                .name("유저A")
                .profilePic("a.jpg")
                .build();

        save_userB = User.builder()
                .id(2L)
                .userId("2")
                .username(usernameB)
                .name("유저B")
                .profilePic("b.jpg")
                .build();
    }

    @Test
    @DisplayName("유저 노드 추가")
    public void addUser() {
        // given
        when(userRepository.save(any())).thenReturn(save_userA);

        // when
        User saved_user = userService.addUser(save_userA);

        // then
        assertThat(saved_user).isEqualTo(saved_user);
    }

    @Test
    @DisplayName("유저 노드 수정")
    public void updateUser() {
        // given
        when(userRepository.save(any())).thenReturn(save_userA);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(save_userA));

        // when
        User saved_user = userService.updateUser(save_userA);

        // then
        assertThat(saved_user).isEqualTo(save_userA);
    }

    @Test
    @DisplayName("팔로우")
    public void follow() {
        // given
        when(userRepository.save(any())).thenReturn(save_userA);
        when(userRepository.findByUserId(any())).thenReturn(Optional.of(save_userA));

        // when
        User saved_user = userService.follow(save_userA, save_userB);

        // then
        assertThat(saved_user).isEqualTo(save_userA);
    }

    @Test
    @DisplayName("팔로워, 팔로잉 수 계산")
    public void findNodeDegree() {
        // given
        when(userRepository.findOutDegree(usernameA)).thenReturn(1L);
        when(userRepository.findInDegree(usernameA)).thenReturn(1L);

        NodeDegree nodeDegree = userService.findNodeDegree(usernameA);

        // then
        assertThat(nodeDegree.getInDegree()).isEqualTo(1L);
        assertThat(nodeDegree.getOutDegree()).isEqualTo(1L);
    }

    @Test
    @DisplayName("팔로잉 여부")
    public void isFollowing() {
        // given
        when(userRepository.isFollowing(usernameA, usernameB)).thenReturn(true);

        // then
        assertThat(userService.isFollowing(usernameA, usernameB)).isEqualTo(true);
    }

    @Test
    @DisplayName("팔로워 출력")
    public void findFollowers() {
        // given
        List<User> users = new LinkedList<>();
        users.add(save_userA);
        when(userRepository.findFollowers(usernameA)).thenReturn(users);

        // then
        assertThat(userService.findFollowers(usernameA).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("팔로워 출력(페이징네이션)")
    public void findPaginatedFollowers() {
        // given
        int page = 0;
        int size = 30;

        Pageable pageable = PageRequest.of(page, size);

        List<User> users = new LinkedList<>();
        users.add(save_userA);

        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findFollowers(usernameA, pageable)).thenReturn(userPage);

        // then
        assertThat(userService.findPaginatedFollowers(usernameA, page, size).getSize()).isEqualTo(1);
    }

    @Test
    @DisplayName("팔로잉 출력")
    public void findFollowing() {
        // given
        List<User> users = new LinkedList<>();
        users.add(save_userA);
        when(userRepository.findFollowing(usernameA)).thenReturn(users);

        // then
        assertThat(userService.findFollowing(usernameA)).isEqualTo(users);
    }

}

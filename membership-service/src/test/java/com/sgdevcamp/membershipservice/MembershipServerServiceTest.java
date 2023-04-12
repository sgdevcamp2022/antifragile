package com.sgdevcamp.membershipservice;

import com.sgdevcamp.membershipservice.model.Profile;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.repository.UserRepository;
import com.sgdevcamp.membershipservice.service.MembershipServerService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.examples.lib.Account;
import net.devh.boot.grpc.examples.lib.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipServerServiceTest {

    @InjectMocks
    private MembershipServerService membershipServerService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetAccount() throws Exception {
        // given
        final String username = "user";
        final String profileImageUrl = "user.jpg";
        Account account = Account.newBuilder()
                .setUsername(username)
                .setProfileImageUrl(profileImageUrl)
                .build();
        StreamObserver streamObserver = mock(StreamObserver.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(User
                .builder()
                .username(username)
                .profile(Profile.builder()
                        .path("user.jpg")
                        .build())
                .build()));

        // when
        membershipServerService.getAccount(Post.newBuilder().setId(anyLong()).build(), streamObserver);

        // then
        verify(streamObserver).onNext(account);
        verify(streamObserver).onCompleted();
    }
}

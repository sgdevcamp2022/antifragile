package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.exception.CustomException;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.repository.UserRepository;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.examples.lib.Account;
import net.devh.boot.grpc.examples.lib.MembershipServiceGrpc;
import net.devh.boot.grpc.examples.lib.Post;
import net.devh.boot.grpc.server.service.GrpcService;

import static com.sgdevcamp.membershipservice.exception.CustomExceptionStatus.ACCOUNT_NOT_FOUND;

@GrpcService
@RequiredArgsConstructor
public class MembershipServerService extends MembershipServiceGrpc.MembershipServiceImplBase {

    private final UserRepository userRepository;

    @Override
    public void getAccount(Post post, StreamObserver<Account> responseObserver) {
        User user = userRepository.findById(post.getId())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        Account response = Account.newBuilder()
                .setUsername(user.getUsername())
                .setProfileImageUrl(user.getProfile().getPath())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
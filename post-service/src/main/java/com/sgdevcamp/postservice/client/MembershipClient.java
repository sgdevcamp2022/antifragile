package com.sgdevcamp.postservice.client;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.examples.lib.Account;
import net.devh.boot.grpc.examples.lib.MembershipServiceGrpc;
import net.devh.boot.grpc.examples.lib.Post;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MembershipClient {

    @GrpcClient("membership-service")
    MembershipServiceGrpc.MembershipServiceBlockingStub membershipStub;

    public Account findProfile(Long id) {
        return membershipStub.getAccount(Post.newBuilder().setId(id).build());
    }

}

package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.dto.response.UserSummary;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final UserRepository userRepository;

    public Page<UserSummary> searchUser(String username, Pageable pageable){

        Page<User> users = userRepository.findByUsernameContaining(username, pageable);

        return users.map(user -> UserSummary.builder()
                .username(user.getUsername())
                .name(user.getName())
                .profilePicture(user.getProfile().getPath())
                .build()
        );
    }
}

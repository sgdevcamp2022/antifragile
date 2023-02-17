package com.sgdevcamp.postservice.service;

import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.User;
import com.sgdevcamp.postservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.ALREADY_EXIST_USER;
import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.NOT_FOUND_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void addUser(User user){

        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            String message = String.format("username %s already exists", user.getUsername());
            log.warn(message);

            throw new CustomException(ALREADY_EXIST_USER);
        }

        User saveUser = userRepository.save(user);

        log.info("user {} save successfully", saveUser.getUsername());
    }

    public User updateUser(User user){

        return userRepository
                .findByUsername(user.getUsername())
                .map(saved_user -> {
                    saved_user.setProfileName(user.getProfileName());
                    saved_user.setImageUrl(user.getImageUrl());
                    saved_user.setIntroduction(user.getIntroduction());

                    saved_user = userRepository.save(saved_user);
                    log.info("user {} updated successfully", saved_user.getUsername());

                    return saved_user;
                })
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }

    public void deleteUser(User user){

        User save_user = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

        userRepository.deleteById(save_user.getId());
    }

}

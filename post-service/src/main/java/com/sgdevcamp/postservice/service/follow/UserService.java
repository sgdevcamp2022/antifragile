package com.sgdevcamp.postservice.service.follow;

import com.sgdevcamp.postservice.dto.follow.response.PagedResult;
import com.sgdevcamp.postservice.exception.CustomException;
import com.sgdevcamp.postservice.model.follow.Friendship;
import com.sgdevcamp.postservice.model.follow.User;
import com.sgdevcamp.postservice.model.follow.NodeDegree;
import com.sgdevcamp.postservice.repository.follow.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.USERNAME_ALREADY_EXIST;
import static com.sgdevcamp.postservice.exception.CustomExceptionStatus.USERNAME_NOT_EXIST;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Service("follow-UserService")
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User addUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()) throw new CustomException(USERNAME_ALREADY_EXIST);

        User save_user = userRepository.save(user);

        log.info("user {} save successfully", save_user.getUsername());

        return save_user;
    }

    public User updateUser(User user){
        return userRepository
                .findByUsername(user.getUsername())
                .map(saved_user -> {
                    saved_user.setName(user.getName());
                    saved_user.setUsername(user.getUsername());
                    saved_user.setProfilePic(user.getProfilePic());

                    saved_user = userRepository.save(saved_user);
                    log.info("user {} updated successfully", saved_user.getUsername());

                    return saved_user;
                })
                .orElseThrow(() -> new CustomException(USERNAME_NOT_EXIST));
    }

    public User follow(User follower, User following){
        log.info("user {} will follow {}",
                follower.getUsername(), following.getUsername());

        User saved_follower = userRepository
                .findByUserId(follower.getUserId())
                .orElseGet(() -> {
                    new CustomException(USERNAME_NOT_EXIST);
                    return this.addUser(follower);
                });

        User saved_following = userRepository
                .findByUserId(following.getUserId())
                .orElseGet(() -> {
                    log.info("user {} not exits, creating it", following.getUsername());
                    return this.addUser(following);
                });

        if(saved_follower.getFriendships() == null){
            saved_follower.setFriendships(new HashSet<>());
        }

        saved_follower
                .getFriendships()
                .add(Friendship.builder()
                        .user(saved_following)
                        .build());

        return userRepository.save(saved_follower);
    }

    public void cancelFollow(String follower, String following){
        userRepository.deleteFollowing(follower, following);

        log.info("{} cancel to follow {}", follower, following);
    }

    public NodeDegree findNodeDegree(String username){
        log.info("fetching degree for user {}", username);

        long out = userRepository.findOutDegree(username);
        long in = userRepository.findInDegree(username);

        log.info("found {} outdegree and {} indegree for user {}", out, in, username);

        return NodeDegree
                .builder()
                .outDegree(out)
                .inDegree(in)
                .build();
    }

    public boolean isFollowing(String user_a, String user_b){
        return userRepository.isFollowing(user_a, user_b);
    }

    public List<User> findFollowers(String username){
        List<User> followers = userRepository.findFollowers(username);
        log.info("found {} followers for user {}", followers.size(), username);

        return followers;
    }

    public PagedResult<User> findPaginatedFollowers(String username, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> followers = userRepository.findFollowers(username, pageable);
        log.info("found {} followers for user {}", followers.getTotalElements(), username);

        return buildPagedResult(followers);
    }

    public PagedResult<User> findPaginatedFollowings(String userId, int page, int size){
        Pageable pageable = PageRequest.of(page, size);

        Page<User> followings = userRepository.findFollowings(userId, pageable);
        log.info("found {} followings for user {}", followings.getTotalElements(), userId);

        return buildPagedResult(followings);
    }

    private PagedResult<User> buildPagedResult(Page<User> page){
        return PagedResult
                .<User>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .page(page.getNumber())
                .size(page.getSize())
                .last(page.isLast())
                .build();
    }

    public List<User> findFollowing(String username){
        List<User> following = userRepository.findFollowing(username);
        log.info("found {} that user {} is following", following.size(), username);

        return following;
    }

   public Map<String, String> usersProfilePic(List<String> usernames){

        List<User> users = userRepository.findByUsernameIn(usernames);

        return users.stream().collect(toMap(User::getUsername,
                User::getProfilePic));
    }

    public User findUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {throw new CustomException(USERNAME_NOT_EXIST);});
    }
}

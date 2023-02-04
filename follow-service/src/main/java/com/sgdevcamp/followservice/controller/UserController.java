package com.sgdevcamp.followservice.controller;

import com.sgdevcamp.followservice.dto.request.FollowRequest;
import com.sgdevcamp.followservice.dto.request.UserDto;
import com.sgdevcamp.followservice.dto.response.CommonResponse;
import com.sgdevcamp.followservice.dto.response.Response;
import com.sgdevcamp.followservice.model.User;
import com.sgdevcamp.followservice.service.ResponseService;
import com.sgdevcamp.followservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow-server")
public class UserController {
    private final UserService userService;
    private final ResponseService responseService;

    private final static String DEFAULT_PAGE_NUMBER = "0";
    private final static String DEFAULT_PAGE_SIZE = "30";

    @PostMapping("/users/followers")
    public Response follow(@RequestBody FollowRequest request){
        log.info("received a follow request follow {} following {}",
                request.getFollower().getUsername(),
                request.getFollowing().getUsername());

        UserDto follower = request.getFollower();
        UserDto following = request.getFollowing();

        User result = userService.follow(
                User.builder()
                        .userId(follower.getId())
                        .username(follower.getUsername())
                        .name(follower.getName())
                        .profilePic(follower.getProfilePicture())
                        .build(),

                User.builder()
                        .userId(following.getId())
                        .username(following.getUsername())
                        .name(following.getName())
                        .profilePic(following.getProfilePicture())
                        .build()
        );

        return responseService.getDataResponse(result);
    }

    @DeleteMapping("/users/{usernameA}/following/{usernameB}")
    public CommonResponse cancelFollowing(@PathVariable String usernameA, @PathVariable String usernameB){
        userService.cancelFollow(usernameA, usernameB);
        return responseService.getSuccessResponse();
    }

    @GetMapping("/users/{username}/degree")
    public Response findNodeDegree(@PathVariable String username){
        log.info("received request to get node degree for {}", username);

        return responseService.getDataResponse(userService.findNodeDegree(username));
    }

    @GetMapping("/users/{usernameA}/following/{usernameB}")
    public Response isFollowing(@PathVariable String usernameA, @PathVariable String usernameB){
        log.info("received request to check is user {} is following {}",
                usernameA, usernameB);

        return responseService.getDataResponse(userService.isFollowing(usernameA, usernameB));
    }

    @GetMapping("/users/{username}/followers")
    public Response findFollowers(@PathVariable String username){
        return responseService.getDataResponse(userService.findFollowers(username));
    }

    @GetMapping("/users/paginated/{username}/followers")
    public Response findFollowersPaginated(
            @PathVariable String username,
            @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {

        return responseService.getDataResponse(userService.findPaginatedFollowers(username, page, size));
    }

    @GetMapping("/users/{username}/following")
    public Response findFollowing(@PathVariable String username){
        return responseService.getDataResponse(userService.findFollowing(username));
    }
}

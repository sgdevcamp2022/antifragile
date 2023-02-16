package com.sgdevcamp.membershipservice.controller;

import com.sgdevcamp.membershipservice.conifg.CustomUserDetails;
import com.sgdevcamp.membershipservice.dto.request.*;
import com.sgdevcamp.membershipservice.dto.response.*;
import com.sgdevcamp.membershipservice.exception.CustomException;
import com.sgdevcamp.membershipservice.exception.CustomExceptionStatus;
import com.sgdevcamp.membershipservice.exception.ValidationExceptionProvider;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.model.UserRole;
import com.sgdevcamp.membershipservice.service.CookieUtil;
import com.sgdevcamp.membershipservice.service.ResponseService;
import com.sgdevcamp.membershipservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sgdevcamp.membershipservice.exception.CustomExceptionStatus.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/membership-server")
public class UserController {
    private final ResponseService responseService;
    private final UserService userService;
    private final CookieUtil cookieUtil;

    @PostMapping("/signup")
    public Response<UserDto> signUp(@RequestBody @Valid UserDto userDto, Errors errors){
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(userService.signUp(userDto));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest, Errors errors){
        if(errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        return responseService.getDataResponse(userService.loginUser(loginRequest));
    }

    @PostMapping("/logout")
    public CommonResponse logout(HttpServletRequest request, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        String accessToken = cookieUtil.getCookie(request, "accessToken").getValue();
        String refreshToken = cookieUtil.getCookie(request, "refreshToken").getValue();

        userService.logout(customUserDetails.getUsername(), accessToken, refreshToken);

        return responseService.getSuccessResponse();
    }

    @PostMapping("/send-mail")
    public Response<MailResponse> sendEmail(@RequestParam(value = "email") String email) {
        return responseService.getDataResponse(userService.sendEmail(email));
    }

    @GetMapping("/check-email")
    public CommonResponse checkEmail(@RequestParam(value = "key") String key) {
        userService.verifyEmail(key);

        return responseService.getSuccessResponse();
    }

    @PatchMapping("/auth/role")
    public CommonResponse updateRole(@RequestParam(value = "email") String email,
                                     @RequestParam(value = "username") String username,
                                     @RequestParam(value = "role") String role) {
        UserRole userRole;

        try {
            userRole = UserRole.valueOf("ROLE_" + role);
        } catch (IllegalArgumentException e) {
            throw new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID_ROLE);
        }
        userService.updateRole(email, username, userRole);
        return responseService.getSuccessResponse();
    }

    @GetMapping("/auth/name/{id}")
    public Response<NameAndPhotoResponse> getNameAndPhotoById(@PathVariable(value = "id") Long id) {
        return responseService.getDataResponse(userService.getNameAndPhoto(id));
    }

    @PostMapping(value = "/auth/summary/in", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserSummaries(@RequestBody List<String> usernames) {
        log.info("retrieving summaries for {} usernames", usernames.size());

        List<UserSummary> summaries =
                userService
                        .findByUsernameIn(usernames)
                        .stream()
                        .map(user -> convertTo(user))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(summaries);

    }

    @GetMapping("/auth/profile")
    public CommonResponse getMyProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        String username = customUserDetails.getUsername();
        if(username == null) throw new CustomException(NOT_AUTHENTICATED_ACCOUNT);

        ProfileResponse profileResponse = userService.getMyProfile(username);

        return responseService.getDataResponse(profileResponse);
    }

    @PatchMapping("/auth/profile")
    public CommonResponse modifyProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @RequestBody @Valid ProfileRequest profileRequest, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);

        ProfileResponse profileResponse = userService.modifyProfile(customUserDetails.getAccount(), profileRequest);

        return responseService.getDataResponse(profileResponse);
    }

    @PostMapping("/auth/profile")
    public CommonResponse uploadProfileImage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestParam("image") MultipartFile multipartFile) throws Exception {
        userService.uploadProfile(customUserDetails.getAccount(), multipartFile);
        return responseService.getSuccessResponse();
    }

    @PostMapping("/password")
    public CommonResponse requestChangePassword(@RequestBody RequestChangePassword1 requestChangePassword1){
        CommonResponse response;
        String username = "";

        try{
            username = requestChangePassword1.getUsername();
            User user = userService.findByUsername(username);

            if(!user.getEmail().equals(requestChangePassword1.getEmail())) throw new NoSuchFieldException("");

            userService.sendMailToChangePassword(user);

            log.info("성공적으로 " + username + "의 비밀번호 변경요청을 수행");

            response = responseService.getSuccessResponse();
        }catch(NoSuchFieldException e){
            log.info("가입된 이메일이 아닙니다.");

            response = responseService.getExceptionResponse(POST_USERS_NOT_EQUAL_EMAIL);
        }
        return response;
    }

    @PostMapping("/password/{key}")
    public CommonResponse isPasswordUUIdValidate(@PathVariable String key){
        CommonResponse response;

        try{
            if(userService.isPasswordUuidValidate(key))
                response = responseService.getSuccessResponse();
            else
                response = responseService.getExceptionResponse(INVALID_KEY);
        }catch (Exception e){
            response = responseService.getExceptionResponse(INVALID_KEY);
        }

        return response;
    }

    @PutMapping("/password")
    public CommonResponse changePassword(@RequestBody RequestChangePassword2 requestChangePassword2){
        String username = "";
        username = requestChangePassword2.getUsername();

        User user = userService.findByUsername(username);
        userService.changePassword(user, requestChangePassword2.getPassword());

        log.info("성공적으로" + username + "의 비밀번호를 변경했습니다.");

        return responseService.getSuccessResponse();
    }

    @DeleteMapping("/auth/remove")
    public CommonResponse removeMember(Principal principal, @RequestBody Map<String, String> map){
        try {
            String username = principal.getName();
            if(userService.isPasswordEqual(username, map.get("password"))) {
                userService.removeMember(username);
                log.info(username + "님이 탈퇴했습니다.");
            }else{
                return responseService.getExceptionResponse(POST_USERS_WRONG_PASSWORD);
            }
        }catch(Exception e){
            log.info("탈퇴에 실패했습니다.");
        }
        return responseService.getSuccessResponse();
    }

    private UserSummary convertTo(User user) {
        return UserSummary
                .builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .name(user.getName())
                .profilePicture(user.getProfile().getPath())
                .build();
    }
}

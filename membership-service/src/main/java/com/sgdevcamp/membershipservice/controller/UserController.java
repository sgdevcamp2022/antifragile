package com.sgdevcamp.membershipservice.controller;

import com.sgdevcamp.membershipservice.conifg.CustomUserDetails;
import com.sgdevcamp.membershipservice.dto.request.LoginRequest;
import com.sgdevcamp.membershipservice.dto.request.ProfileRequest;
import com.sgdevcamp.membershipservice.dto.request.UserDto;
import com.sgdevcamp.membershipservice.dto.response.*;
import com.sgdevcamp.membershipservice.exception.CustomException;
import com.sgdevcamp.membershipservice.exception.CustomExceptionStatus;
import com.sgdevcamp.membershipservice.exception.ValidationExceptionProvider;
import com.sgdevcamp.membershipservice.model.UserRole;
import com.sgdevcamp.membershipservice.service.ResponseService;
import com.sgdevcamp.membershipservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/membership-server")
public class UserController {
    private final ResponseService responseService;
    private final UserService userService;

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

    @PatchMapping("/auth/profile")
    public CommonResponse modifyProfile(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                        @RequestBody @Valid ProfileRequest profileRequest, Errors errors) {
        if (errors.hasErrors()) ValidationExceptionProvider.throwValidError(errors);
        userService.modifyProfile(customUserDetails.getAccount(), profileRequest);
        return responseService.getSuccessResponse();
    }

    @PostMapping("/auth/profile")
    public CommonResponse uploadProfileImage(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                             @RequestParam("image") MultipartFile multipartFile) throws Exception {
        userService.uploadProfile(customUserDetails.getAccount(), multipartFile);
        return responseService.getSuccessResponse();
    }
}

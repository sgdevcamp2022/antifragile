package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.dto.request.LoginRequest;
import com.sgdevcamp.membershipservice.dto.request.ProfileRequest;
import com.sgdevcamp.membershipservice.dto.request.UserDto;
import com.sgdevcamp.membershipservice.dto.response.LoginResponse;
import com.sgdevcamp.membershipservice.dto.response.MailResponse;
import com.sgdevcamp.membershipservice.dto.response.NameAndPhotoResponse;
import com.sgdevcamp.membershipservice.exception.CustomException;
import com.sgdevcamp.membershipservice.exception.CustomExceptionStatus;
import com.sgdevcamp.membershipservice.model.Profile;
import com.sgdevcamp.membershipservice.model.Salt;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.model.UserRole;
import com.sgdevcamp.membershipservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final FileService fileService;
    private final SaltUtil saltUtil;

    @Transactional
    public UserDto signUp(UserDto signupForm){
        if (userRepository.findByUsername(signupForm.getUsername()).isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_USERNAME);
        else if(userRepository.findByEmail(signupForm.getEmail()).isPresent()) throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);

        String password = signupForm.getPassword();
        String salt = saltUtil.genSalt();

        User account = User.builder()
                .email(signupForm.getEmail())
                .username(signupForm.getUsername())
                .password(saltUtil.encodePassword(salt, password))
                .name(signupForm.getName())
                .profile(signupForm.getProfile())
                .role(UserRole.ROLE_NOT_PERMITTED)
                .introduction(signupForm.getIntroduction())
                .access_time(LocalDateTime.now())
                .salt(Salt.builder().salt(salt).build())
                .build();

        User save = userRepository.save(account);
        signupForm.setId(save.getId());

        return signupForm;
    }

    @Transactional
    public LoginResponse loginUser(LoginRequest request) {
        User account = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN));

        String salt = account.getSalt().getSalt();
        String password = saltUtil.encodePassword(salt, request.getPassword());

        if (!account.getPassword().equals(password)) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .role(account.getRole())
                .build();

        String refreshToken = jwtUtil.generateRefreshToken(user);
        if(request.getEmail() == null) redisUtil.setDataExpire(refreshToken, request.getUsername(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        else redisUtil.setDataExpire(refreshToken, request.getEmail(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);

        LoginResponse res = LoginResponse.builder()
                .id(account.getId())
                .username(account.getName())
                .email(account.getEmail())
                .accessToken(jwtUtil.generateToken(user))
                .refreshToken(refreshToken)
                .build();

        return res;
    }

    @Transactional
    public MailResponse sendEmail(String email) {
        String VERIFICATION_LINK = "http://localhost:5555/membership-server/verify";
        User exist = userRepository.findByEmailAndRoleLike(email, UserRole.ROLE_USER).orElse(null);
        if (exist != null) {
            throw new CustomException(CustomExceptionStatus.DUPLICATED_EMAIL);
        }

        if (email == null) {
            throw new CustomException(CustomExceptionStatus.POST_USERS_EMPTY_EMAIL);
        }

        String uuid = UUID.randomUUID().toString();
        redisUtil.setDataExpire(uuid.toString(), email, 60 * 30L);

        emailService.sendMail(email,"[ Instagram ] 회웝가입 인증이메일", VERIFICATION_LINK + uuid);

        MailResponse res = MailResponse.builder()
                .code(uuid).build();

        return res;
    }

    @Transactional
    public void verifyEmail(String key) {
        String email = redisUtil.getData(key);
        if (email == null) {
            throw new CustomException(CustomExceptionStatus.NOT_VALID_CODE);
        } else {
            redisUtil.deleteData(key);
        }
    }

    @Transactional
    public void updateRole(String email, String username, UserRole userRole) {
        User user = userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_VALID));
        user.updateRole(userRole);
    }

    @Transactional
    public void modifyProfile(User user, ProfileRequest profileRequest) {
        user.updateIntroduction(profileRequest.getIntroduction());
        userRepository.save(user);
    }

    public NameAndPhotoResponse getNameAndPhoto(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        NameAndPhotoResponse result = NameAndPhotoResponse.builder()
                .name(user.getName())
                .instruction(user.getIntroduction())
                .profile(user.getProfile())
                .build();

        return result;
    }

    @Transactional
    public void uploadProfile(User user, MultipartFile file) throws Exception{
        Path rootLocation= Paths.get("C:/image/");
        String save_file_name = fileService.fileSave(rootLocation.toString(), file);

        Profile profile = Profile.builder()
                .original_file_name(file.getOriginalFilename())
                .server_file_name(save_file_name)
                .path(rootLocation.toString().replace(File.separatorChar, '/') +'/' + save_file_name)
                .size(file.getResource().contentLength())
                .update_at(LocalDateTime.now())
                .build();

        user.updateProfile(profile);
    }


}

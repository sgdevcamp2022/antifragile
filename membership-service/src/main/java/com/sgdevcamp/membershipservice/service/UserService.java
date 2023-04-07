package com.sgdevcamp.membershipservice.service;

import com.sgdevcamp.membershipservice.conifg.MyUserDetailsService;
import com.sgdevcamp.membershipservice.dto.request.LoginRequest;
import com.sgdevcamp.membershipservice.dto.request.ProfileRequest;
import com.sgdevcamp.membershipservice.dto.request.UserDto;
import com.sgdevcamp.membershipservice.dto.response.*;
import com.sgdevcamp.membershipservice.exception.CustomException;
import com.sgdevcamp.membershipservice.exception.CustomExceptionStatus;
import com.sgdevcamp.membershipservice.messaging.UserEventSender;
import com.sgdevcamp.membershipservice.model.Profile;
import com.sgdevcamp.membershipservice.model.Salt;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.model.UserRole;
import com.sgdevcamp.membershipservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.sgdevcamp.membershipservice.exception.CustomExceptionStatus.ACCOUNT_NOT_FOUND;
import static com.sgdevcamp.membershipservice.exception.CustomExceptionStatus.INVALID_JWT;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final S3Service s3Service;
    private final SaltUtil saltUtil;
    private final RedisTemplate redisTemplate;
    private final UserEventSender userEventSender;
    private final MyUserDetailsService myUserDetailsService;
    private final Profile default_profile = Profile.builder()
            .path("https://instagramimages16.s3.ap-northeast-2.amazonaws.com/%EA%B8%B0%EB%B3%B8+%ED%94%84%EC%82%AC.jfif")
            .build();

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
                .profile(signupForm.getProfile() != null ? signupForm.getProfile() : default_profile)
                .role(UserRole.ROLE_NOT_PERMITTED)
                .introduction(signupForm.getIntroduction())
                .access_time(LocalDateTime.now())
                .salt(Salt.builder().salt(salt).build())
                .build();

        User save = userRepository.save(account);
        userEventSender.sendUserCreated(save);

        signupForm.setId(save.getId());

        log.info("{} 회원가입이 완료되었습니다.", save.getUsername());

        return signupForm;
    }

    public User loginUser(LoginRequest request) {

        User account = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        String salt = account.getSalt().getSalt();
        String password = saltUtil.encodePassword(salt, request.getPassword());

        if (!account.getPassword().equals(password)) {
            throw new CustomException(CustomExceptionStatus.FAILED_TO_LOGIN);
        }

        return account;
    }

    public void logout(String username, String accessToken, String refreshToken) {
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

        if (!jwtUtil.validateToken(accessToken, userDetails)) throw new CustomException(INVALID_JWT);

        if (redisTemplate.opsForValue().get(refreshToken) != null) {
            redisTemplate.delete(refreshToken);
        }

        Long expiration = jwtUtil.getExpiredTime(accessToken).getTime();
        redisTemplate.opsForValue()
                .set(accessToken, "logout", expiration, TimeUnit.MILLISECONDS);

        log.info("{} 로그아웃 되었습니다.", username);
    }

    public LoginResponse checkRefreshToken(String refresh_token){
        String username = redisUtil.getData(refresh_token);

        if(username == null){
            throw new CustomException(INVALID_JWT);
        }

        User account = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        User user = User.builder()
                .username(username)
                .email(account.getEmail())
                .role(account.getRole())
                .build();

        LoginResponse res = LoginResponse.builder()
                .accessToken(jwtUtil.generateToken(user))
                .build();

        return res;
    }

    public User findByUsername(String username) {
        User member = userRepository.findByUsername(username)
                .orElseThrow(() -> {throw new CustomException(ACCOUNT_NOT_FOUND);});
        return member;
    }

    public List<User> findByUsernameIn(List<String> usernames) {
        return userRepository.findByUsernameIn(usernames);
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

    public ProfileResponse getMyProfile(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.ACCOUNT_NOT_FOUND));

        return ProfileResponse.builder()
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .introduction(user.getIntroduction())
                .build();
    }

    public ProfileResponse modifyProfile(User user, ProfileRequest profileRequest) {
        user.updateNameAndIntroduction(user.getName(), profileRequest.getIntroduction());

        User saved_user = userRepository.save(user);

        log.info("{} has modified Profile", user.getUsername());

        return ProfileResponse.builder()
                .name(saved_user.getName())
                .introduction(saved_user.getIntroduction())
                .build();
    }

    public NameAndPhotoResponse getNameAndPhoto(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_FOUND));

        NameAndPhotoResponse result = NameAndPhotoResponse.builder()
                .name(user.getName())
                .instruction(user.getIntroduction())
                .profile(user.getProfile())
                .build();

        return result;
    }

    public String uploadProfile(User user, MultipartFile file) throws Exception{

        Profile save_profile = s3Service.uploadMediaToS3(file, user.getUsername());

        String old_image = user.getProfile().getPath();
        user.updateProfile(save_profile);
        userRepository.save(user);

        userEventSender.sendUserUpdated(user, old_image);

        return save_profile.getPath();
    }

    @Transactional
    public void sendMailToChangePassword(User user) {
        String CHANGE_PASSWORD_LINK = "http://localhost:5555/password/";

        if(user == null) throw new CustomException(ACCOUNT_NOT_FOUND);

        String key = UUID.randomUUID().toString();
        redisUtil.setDataExpire(key, user.getUsername(), 60 * 30L);
        emailService.sendMail(user.getEmail(), "사용자 비밀번호 안내 메일", CHANGE_PASSWORD_LINK + key);
    }

    public boolean isPasswordUuidValidate(String key){
        String memberId = redisUtil.getData(key);
        return !memberId.equals("");
    }

    @Transactional
    public void changePassword(User user, String password) {
        if(user == null) throw new CustomException(ACCOUNT_NOT_FOUND);
        String salt = saltUtil.genSalt();
        user.updateSaltAndPassword(new Salt(salt), saltUtil.encodePassword(salt, password));
    }

    public boolean isPasswordEqual(String username, String requestPwd){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {throw new CustomException(ACCOUNT_NOT_FOUND);});

        String salt = user.getSalt().getSalt();
        String encodeRequest = saltUtil.encodePassword(salt, requestPwd);

        if(!user.getPassword().equals(encodeRequest)){
            return false;
        }

        return true;
    }

    public void removeMember(String username){
        userRepository.deleteByUsername(username);
    }
}

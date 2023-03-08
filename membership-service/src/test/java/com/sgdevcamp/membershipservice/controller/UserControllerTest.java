package com.sgdevcamp.membershipservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgdevcamp.membershipservice.conifg.JwtRequestFilter;
import com.sgdevcamp.membershipservice.dto.request.LoginRequest;
import com.sgdevcamp.membershipservice.dto.response.LoginResponse;
import com.sgdevcamp.membershipservice.dto.response.Response;
import com.sgdevcamp.membershipservice.model.User;
import com.sgdevcamp.membershipservice.service.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtRequestFilter.class, WebSecurityConfigurerAdapter.class})
})
@AutoConfigureDataJpa
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    ResponseService responseService;

    @MockBean
    CookieUtil cookieUtil;

    @MockBean
    JwtUtil jwtUtil;

    @MockBean
    RedisUtil redisUtil;

    static String email;

    static String username;

    static String name;

    static String password;

    static Response response;

    @BeforeAll
    public static void beforeAll(){
        email = "test@test.com";
        username = "java00";
        password = "helloworld";
        name = "자바";

        response = new Response<>();
        response.setIsSuccess(true);
        response.setCode(1000);
        response.setMessage("요청에 성공하였습니다.");
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception{

        final String ACCESS_TOKEN = "access_token";
        final String REFRESH_TOKEN = "refresh_token";
        String cookie_name = "refreshToken";

        String loginRequest = objectMapper.writeValueAsString(LoginRequest.builder()
                .email(email)
                .password(password)
                .build()
        );

        User user = User.builder()
                .id(1L)
                .build();

        LoginResponse loginResponse = LoginResponse.builder()
                .id(1L)
                .accessToken(ACCESS_TOKEN)
                .build();

        response.setResult(loginResponse);

        when(userService.loginUser(any())).thenReturn(user);
        when(jwtUtil.generateToken(any())).thenReturn(ACCESS_TOKEN);
        when(jwtUtil.generateRefreshToken(any())).thenReturn(REFRESH_TOKEN);
        when(cookieUtil.createCookie(anyString(), anyString())).thenReturn(new MockCookie(cookie_name, REFRESH_TOKEN));
        when(responseService.getDataResponse(any())).thenReturn(response);

        mockMvc.perform(post("/membership-server/login")
                        .content(loginRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpectAll(status().isOk(),
                        jsonPath("$.result.id").value(loginResponse.getId()),
                        jsonPath("$.result.accessToken").value(loginResponse.getAccessToken()),
                        cookie().value(cookie_name, REFRESH_TOKEN)
                );
    }

    @Test
    @DisplayName("로그인 - 유효성 검사 실패(이메일 형식)")
    void loginThrowValidationException() throws Exception{

        String loginRequest = objectMapper.writeValueAsString(LoginRequest.builder()
                .email("test")
                .password(password)
                .build()
        );

        mockMvc.perform(post("/membership-server/login")
                        .content(loginRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andDo(print())
                .andExpect((result) -> assertTrue(result.getResolvedException()
                        .getClass().isAssignableFrom(MethodArgumentNotValidException.class)));
    }

}

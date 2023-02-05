package com.sgdevcamp.membershipservice.conifg;

import com.sgdevcamp.membershipservice.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final JwtRequestFilter jwtRequestFilter;

    private final CustomOAuth2UserService oAuthService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                    .authorizeRequests()
                        .antMatchers("/membership-server/signup", "/membership-server/login",
                                "/membership-service/check-email", "/membership-service/password/*").permitAll()
                        .anyRequest().authenticated()
                .and()
                    .logout()
                    .logoutUrl("/users/logout")
                    .deleteCookies(("refreshToken"))
                .and()
                    .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(oAuthService)
                .and()
                    .successHandler(oAuthService::onAuthenticationSuccess)
                    .failureUrl("http://localhost:5555/users/login")
                .and()
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

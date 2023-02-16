package com.sgdevcamp.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class CustomAuthFilter extends AbstractGatewayFilterFactory<CustomAuthFilter.Config>{
    @Value("${spring.security.jwt.secret}")
    private String SECRET_KEY;

    public static class Config {

    }

    public CustomAuthFilter(){
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            HttpHeaders headers = request.getHeaders();

            List<String> cookieHeaders = headers.get(HttpHeaders.COOKIE);
            String accessToken = new String();
            String refreshToken = new String();
            if (cookieHeaders != null) {
                for (String cookieHeader : cookieHeaders) {
                    String[] cookies = cookieHeader.split(";");
                    for (String cookie : cookies) {
                        String[] parts = cookie.trim().split("=");
                        if (parts.length == 2) {
                            String name = parts[0];
                            String value = parts[1];
                            if(name.equals("accessToken")){
                                accessToken = value;
                            }else if(name.equals("refreshToken")){
                                refreshToken = value;
                            }
                        }
                    }
                }
            }

            if (accessToken.isEmpty()){
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }

            String jwt = accessToken;

            if (!isJwtValid(jwt)) {
                log.info("fail");
                return onError(exchange,"JWT token is not valid",HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }
    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String subject = null;

        try {
            subject = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (Exception ex) {
            returnValue = false;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(err);
        return response.setComplete();
    }

}

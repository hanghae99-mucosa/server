package com.hanghae99.mocosa.config.jwt;

public interface JwtProperties {
    String SECRET = "MUCOSA";
    int EXPIRATION_TIME = 3600000; // 1시간 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}

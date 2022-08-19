package com.hanghae99.mocosa.config.jwt;

public interface JwtProperties {
    String SECRET = "MUCOSA";
    int EXPIRATION_TIME = 1800000; // 30분 (1/1000초)
    String TOKEN_PREFIX_SEND = "Bearer ";
    String TOKEN_PREFIX_RECIEVE = "token=Bearer%20";
    String HEADER_STRING_SEND = "Authorization";
    String HEADER_STRING_RECIEVE = "Cookie";
}

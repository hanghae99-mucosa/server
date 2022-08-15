package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.layer.dto.user.SignupResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private SignupDto userForSignup = SignupDto.builder()
            .email("abc@naver.com")
            .password("abc123123*")
            .passwordCheck("abc123123*")
            .role("유저")
            .build();

    private SigninDto userForSignin = SigninDto.builder()
            .email("abc@naver.com")
            .password("abc123123*")
            .build();


    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("Case 1: 유저 등록 성공 케이스")
    void TODO_SIGNUP_RESULT_SUCCESS() throws JsonProcessingException {
        // given

        String requestBody = mapper.writeValueAsString(userForSignup);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<SignupResponseDto> response = restTemplate.postForEntity(
                "/api/signup",
                request,
                SignupResponseDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SignupResponseDto signupResponseDto = response.getBody();
        assertNotNull(signupResponseDto);
        assertEquals(signupResponseDto.getMessage(), "회원가입에 성공했습니다.");
    }

    @Test
    @DisplayName("Case 2: 형식에 맞지 않는 유저 등록 실패 케이스")
    void TODO_SIGNUP_WRONG_EMAIL_RESULT_FAIL() throws JsonProcessingException {
        // given
        SignupDto user = SignupDto.builder()
                .email("naver")
                .password("abc123123")
                .passwordCheck("abc123123*")
                .role("유저")
                .build();

        String requestBody = mapper.writeValueAsString(user);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/api/signup",
                request,
                ErrorResponseDto.class
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals(errorResponseDto.getMessage(), ErrorCode.SIGNUP_BAD_ID.getErrorMessage());
    }

    @Test
    @DisplayName("Case 3: 중복된 유저 등록 실패 케이스")
    void TODO_SIGNUP_DUPLICATED_EMAIL_RESULT_FAIL() throws JsonProcessingException {
        // given
        SignupDto user = SignupDto.builder()
                .email("test1@test.com")
                .password("abc123123")
                .passwordCheck("abc123123*")
                .role("유저")
                .build();

        String requestBody = mapper.writeValueAsString(user);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/api/signup",
                request,
                ErrorResponseDto.class
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals(errorResponseDto.getMessage(), ErrorCode.SIGNUP_DUPLICATE_ID.getErrorMessage());
    }

    @Test
    @DisplayName("Case 4: 잘못된 비밀번호 유저 등록 실패 케이스")
    void TODO_SIGNUP_WRONG_PASSWORD_RESULT_FAIL() throws JsonProcessingException {
        // given
        SignupDto user = SignupDto.builder()
                .email("abcd@naver.com")
                .password("a")
                .passwordCheck("a")
                .role("유저")
                .build();

        String requestBody = mapper.writeValueAsString(user);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<ErrorResponseDto> response = restTemplate.postForEntity(
                "/api/signup",
                request,
                ErrorResponseDto.class
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponseDto errorResponseDto = response.getBody();
        assertNotNull(errorResponseDto);
        assertEquals(errorResponseDto.getMessage(), ErrorCode.SIGNUP_BAD_PASSWORD.getErrorMessage());
    }

    @Test
    @DisplayName("Case 5: 유저 로그인 성공 케이스")
    void TODO_SIGNIN_RESULT_SUCCESS() throws JsonProcessingException {
        // given

        String requestBody = mapper.writeValueAsString(userForSignin);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<SigninResponseDto> response = restTemplate.postForEntity(
                "/api/signin",
                request,
                SigninResponseDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SigninResponseDto signinResponseDto = response.getBody();
        assertNotNull(signinResponseDto);
        assertEquals(userForSignin.email, signinResponseDto.getEmail());
        assertNotNull(signinResponseDto.getToken());
    }

    @Getter
    @Setter
    @Builder
    static class SignupDto {
        private String email;
        private String password;
        private String passwordCheck;
        private String role;
    }

    @Getter
    @Setter
    @Builder
    static class SigninDto {
        public String email;
        public String password;
    }

    @Getter
    @Setter
    @Builder
    static class SigninResponseDto {
        public String email;
        public String token;
    }

    @Getter
    @Setter
    @Builder
    static class FailResponseDto {
        public String message;
    }
}

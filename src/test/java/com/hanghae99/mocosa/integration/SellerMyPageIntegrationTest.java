package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.hanghae99.mocosa.config.exception.code.ErrorCode.MYPAGE_NO_DATA;
import static com.hanghae99.mocosa.config.exception.code.ErrorCode.RESTOCK_BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SellerMyPageIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    private String userToken1;
    private UserIntegrationTest.SigninDto userForSignin = UserIntegrationTest.SigninDto.builder()
            .email("test1@test.com")
            .password("abc123123*")
            .build();


    @BeforeEach
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("유저 로그인 성공 케이스")
    void TODO_SIGNIN_RESULT_SUCCESS() throws JsonProcessingException {
        // given

        String requestBody = mapper.writeValueAsString(userForSignin);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        // when
        ResponseEntity<UserIntegrationTest.SigninResponseDto> response = restTemplate.postForEntity(
                "/signin",
                request,
                UserIntegrationTest.SigninResponseDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserIntegrationTest.SigninResponseDto signinResponseDto = response.getBody();
        assertNotNull(signinResponseDto);
        assertEquals(userForSignin.email, signinResponseDto.getEmail());
        assertNotNull(signinResponseDto.getToken());

        userToken1 = signinResponseDto.getToken();
    }

    @Test
    @DisplayName("재입고 가능 상품 목록을 성공적으로 가져온 경우")
    public void case1(){
        //given

        //when
        headers.set("Authorization", userToken1);
        ResponseEntity<RestockListResponseDto[]> response = restTemplate
                .getForEntity(
                        "/users/restock",
                        RestockListResponseDto[].class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<RestockListResponseDto> responseBody = Arrays.asList(response.getBody());
        assertNotNull(responseBody);

        assertEquals(
                8L
                ,responseBody.get(0).productId);
        assertEquals(
                "[쿨탠다드] 우먼즈 스트레이트 히든 밴딩 슬랙스 [블랙]"
                ,responseBody.get(0).productName);

        assertEquals(
                2L
                ,responseBody.get(1).productId);
        assertEquals(
                "K87 워크웨어 포켓 반팔티셔츠 (20colors)"
                ,responseBody.get(1).productName);
    }

    @Test
    @DisplayName("수량이 0개인 상품이 없는 경우")
    public void case2(){
        //given

        //when
        ResponseEntity<ExceptionResponseDto> response = restTemplate
                .getForEntity(
                        "/users/restock",
                        ExceptionResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ExceptionResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals(MYPAGE_NO_DATA.toString(), responseBody.getCode());
        assertEquals("가져올 데이터가 없습니다.", responseBody.getMessage());
    }

    @Test
    @DisplayName("재입고 등록에 성공한 경우")
    public void case3() throws JsonProcessingException {
        //given
        RestockRequestDto restockRequestDto = new RestockRequestDto(2L, 80);

        String requestBody = mapper.writeValueAsString(restockRequestDto);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        //when
        ResponseEntity<RestockResponseDto> response = restTemplate
                .exchange(
                        "/users/restock",
                        HttpMethod.PUT,
                        request,
                        RestockResponseDto.class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        RestockResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals(
                "재입고 등록이 완료되었습니다."
                ,responseBody.getMessage());
    }

    @Test
    @DisplayName("수량을 0개로 요청하는 경우")
    public void case4() throws JsonProcessingException {
        //given
        RestockRequestDto restockRequestDto = new RestockRequestDto(2L, 0);

        String requestBody = mapper.writeValueAsString(restockRequestDto);
        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .exchange(
                        "/users/restock",
                        HttpMethod.PUT,
                        request,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals(RESTOCK_BAD_REQUEST.toString(), responseBody.getCode());
        assertEquals("재입고 등록은 1개 이상부터 가능합니다.", responseBody.getMessage());
    }

    @Getter
    @Builder
    static class RestockListResponseDto {
        private Long productId;
        private String productName;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    static class RestockRequestDto {
        private Long productId;
        private int amount;
    }

    @Getter
    @NoArgsConstructor
    static class RestockResponseDto {
        private String message;
    }

    @Getter
    @Builder
    static class ExceptionResponseDto{
        private LocalDateTime timestamp = LocalDateTime.now();
        private int status;
        private String error;
        private String code;
        private String message;
    }
}

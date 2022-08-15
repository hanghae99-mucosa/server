package com.hanghae99.mocosa.integration;

import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.hanghae99.mocosa.config.exception.code.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserMyPageIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("주문 데이터 가져오기에 성공한 케이스")
    public void case1(){
        //given
        int page = 0;

        //when
        ResponseEntity<OrderHistoryResponseDtoList> response = restTemplate
                .getForEntity(
                        "/users/orders?page=" + page,
                        OrderHistoryResponseDtoList.class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<OrderHistoryResponseDto> responseBody = response.getBody().content;
        assertNotNull(responseBody);

        assertEquals(
                1L
                ,responseBody.get(0).orderId);
//        assertEquals(
//                "2022-08-12 19:23:40.314995"
//                ,responseBody.get(0).createdAt);
        assertEquals(
                "릴렉스 핏 크루 넥 반팔 티셔츠"
                ,responseBody.get(0).productName);
        assertEquals(
                "image.png"
                ,responseBody.get(0).thumbnail);
        assertEquals(
                "무신사 무탠다드"
                ,responseBody.get(0).brandName);
        assertEquals(
                "상의"
                ,responseBody.get(0).category);
        assertEquals(
                1069000
                ,responseBody.get(0).totalPrice);
        assertEquals(
                100
                ,responseBody.get(0).orderAmount);
    }

    @Test
    @DisplayName("주문 데이터가 없는 경우")
    public void case2(){
        //given
        int page = 0;

        //when
        ResponseEntity<ExceptionResponseDto> response = restTemplate
                .getForEntity(
                        "/users/orders?page=" + page,
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
    @DisplayName("존재하지 않는 페이지를 요청하는 경우")
    public void case3(){
        //given
        int page = 100000;

        //when
        ResponseEntity<ExceptionResponseDto> response = restTemplate
                .getForEntity(
                        "/users/orders?page=" + page,
                        ExceptionResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ExceptionResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals(MYPAGE_NO_PAGE.toString(), responseBody.getCode());
        assertEquals("존재하지 않는 페이지입니다.", responseBody.getMessage());
    }



    @Getter
    static class OrderHistoryResponseDtoList {
        private List<OrderHistoryResponseDto> content;
    }

    @Getter
    @Builder
    static class OrderHistoryResponseDto {
        private Long orderId;
        private String createdAt;
        private String productName;
        private String thumbnail;
        private String brandName;
        private String category;
        private int totalPrice;
        private int orderAmount;
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

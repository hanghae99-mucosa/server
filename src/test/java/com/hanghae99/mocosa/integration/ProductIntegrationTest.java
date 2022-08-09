package com.hanghae99.mocosa.integration;

import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("검색에 성공한 케이스 - 필터 제외")
    public void case1(){
        //given
        String keyword = "무탠다드";

        //when
        ResponseEntity<SearchResponseDto> response = restTemplate
                .getForEntity(
                        "/api/search?keyword="+keyword,
                        SearchResponseDto.class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SearchResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals(
                1L
                ,responseBody.getProductId());
        assertEquals(
                "릴렉스 핏 크루 넥 반팔 티셔츠"
                ,responseBody.getName());
        assertEquals(
                "image.png"
                ,responseBody.getThumbnail());
        assertEquals(
                "무신사 스탠다드"
                ,responseBody.getBrandName());
        assertEquals(
                "상의"
                ,responseBody.getCategory());
        assertEquals(
                10690
                ,responseBody.getPrice());
        assertEquals(
                100
                ,responseBody.getAmount());
        assertEquals(
                69058
                ,responseBody.getReviewNum());
        assertEquals(
                4.8
                ,responseBody.getReviewAvg());
    }

    @Test
    @DisplayName("검색에 성공한 케이스 - 필터 존재")
    public void case2(){
        //given
        String keyword = "무탠다드";
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;

        //when
        ResponseEntity<SearchResponseDto> response = restTemplate
                .getForEntity(
                        "/api/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        SearchResponseDto.class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        SearchResponseDto responseBody = response.getBody();
        assertNotNull(responseBody);

        assertEquals(
                1L
                ,responseBody.getProductId());
        assertEquals(
                "릴렉스 핏 크루 넥 반팔 티셔츠"
                ,responseBody.getName());
        assertEquals(
                "image.png"
                ,responseBody.getThumbnail());
        assertEquals(
                "무신사 스탠다드"
                ,responseBody.getBrandName());
        assertEquals(
                "상의"
                ,responseBody.getCategory());
        assertEquals(
                10690
                ,responseBody.getPrice());
        assertEquals(
                100
                ,responseBody.getAmount());
        assertEquals(
                69058
                ,responseBody.getReviewNum());
        assertEquals(
                4.8
                ,responseBody.getReviewAvg());
    }

    @Test
    @DisplayName("검색 결과가 존재하지 않는 케이스")
    public void case3(){
        //given
        String keyword = "무탠다드";
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .getForEntity(
                        "/api/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();

        assert responseBody != null;
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals("상품이 존재하지 않습니다.", responseBody.getMessage());
    }

    @Test
    @DisplayName("키워드가 공란이여서 실패하는 케이스")
    public void case4(){
        //given
        String keyword = "";
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .getForEntity(
                        "/api/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();

        assert responseBody != null;
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals("키워드를 입력해주세요.", responseBody.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 페이지를 요청해서 실패하는 케이스")
    public void case5(){
        //given
        String keyword = "무탠다드";
        int page = -1;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .getForEntity(
                        "/api/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();

        assert responseBody != null;
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals("잘못된 접근입니다.", responseBody.getMessage());
    }


    @Getter
    @Builder
    static class SearchResponseDto{
        private Long productId;
        private String name;
        private String thumbnail;
        private String brandName;
        private String category;
        private int price;
        private int amount;
        private int reviewNum;
        private float reviewAvg;
    }

}

package com.hanghae99.mocosa.integration;

import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


import java.util.List;

import static com.hanghae99.mocosa.config.exception.code.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SearchIntegrationTest {

    static final double TOLERANCE = 0.001;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    @DisplayName("검색에 성공한 케이스 - 필터 제외")
    public void TODO_GET_SEARCH_RESULT_WITHOUT_FILTER_RESULT_LIST_DTO(){
        //given
        String keyword = "티셔츠";

        //when
        ResponseEntity<SearchResponseDtoList> response = restTemplate
                .getForEntity(
                        "/api/search?keyword=" + keyword,
                        SearchResponseDtoList.class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SearchResponseDto> responseBody = response.getBody().content;
        assertNotNull(responseBody);

        assertEquals(
                1L
                ,responseBody.get(0).productId);
        assertEquals(
                "릴렉스 핏 크루 넥 반팔 티셔츠"
                ,responseBody.get(0).name);
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
                10690
                ,responseBody.get(0).price);
        assertEquals(
                100
                ,responseBody.get(0).amount);
        assertEquals(
                69058
                ,responseBody.get(0).reviewNum);
        assertEquals(
                4.8
                ,responseBody.get(0).reviewAvg, TOLERANCE);
    }

    @Test
    @DisplayName("검색에 성공한 케이스 - 필터 존재")
    public void TODO_GET_SEARCH_RESULT_WITH_FILTER_RESULT_LIST_DTO(){
        //given
        String keyword = "티셔츠";
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;

        //when
        ResponseEntity<SearchResponseDtoList> response = restTemplate
                .getForEntity(
                        "/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        SearchResponseDtoList.class
                );

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<SearchResponseDto> responseBody = response.getBody().content;
        assertNotNull(responseBody);

        assertEquals(
                1L
                ,responseBody.get(0).productId);
        assertEquals(
                "릴렉스 핏 크루 넥 반팔 티셔츠"
                ,responseBody.get(0).name);
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
                10690
                ,responseBody.get(0).price);
        assertEquals(
                100
                ,responseBody.get(0).amount);
        assertEquals(
                69058
                ,responseBody.get(0).reviewNum);
        assertEquals(
                4.8
                ,responseBody.get(0).reviewAvg, TOLERANCE);
    }

    @Test
    @DisplayName("검색 결과가 존재하지 않는 케이스")
    public void TODO_NO_SEARCH_RESULT_RESULT_ERROR(){
        //given
        String keyword = "무탠다드 에디션";
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 5F;

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .getForEntity(
                        "/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();

        assert responseBody != null;
        assertEquals(SEARCH_NO_PRODUCT.toString(), responseBody.getCode());
        assertEquals("상품이 존재하지 않습니다.", responseBody.getMessage());
    }

    @Test
    @DisplayName("키워드가 공란이여서 실패하는 케이스")
    public void TODO_BLANK_KEYWORD_RESULT_ERROR(){
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
                        "/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();

        assert responseBody != null;
        assertEquals(SEARCH_BLANK_KEYWORD.toString(), responseBody.getCode());
        assertEquals("키워드를 입력해주세요.", responseBody.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 페이지를 요청해서 실패하는 케이스")
    public void TODO_NO_PAGE_RESULT_ERROR(){
        //given
        String keyword = "무탠다드";
        int page = 10000;  //2147483647
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .getForEntity(
                        "/search?page=" + page + "&sort=" + sort + "&categoryFilter=" + categoryFilter
                                + "&minPriceFilter=" + minPriceFilter + "&maxPriceFilter=" + maxPriceFilter
                                + "&reviewFilter=" + reviewFilter + "&keyword=" + keyword,
                        ErrorResponseDto.class
                );

        //then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponseDto responseBody = response.getBody();

        assert responseBody != null;
        assertEquals(SEARCH_BAD_REQUEST.toString(), responseBody.getCode());
        assertEquals("잘못된 접근입니다.", responseBody.getMessage());
    }

    @Getter
    @ToString
    static class SearchResponseDtoList {
        private List<SearchResponseDto> content;
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

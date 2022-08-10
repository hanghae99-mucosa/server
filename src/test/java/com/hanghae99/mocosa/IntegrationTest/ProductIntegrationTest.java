package com.hanghae99.mocosa.IntegrationTest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.hanghae99.mocosa.MocosaApplication;
import lombok.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration(classes = MocosaApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("검색에 성공한 경우 - 메인페이지")
    @Order(1)
    void case1_1() {
        //given
        String keyword = "무탠다드";
        String queryString = "?keyword=" + keyword;

        //when
        ResponseEntity<RestPageImpl> responseEntity
                = restTemplate.getForEntity(
                        "/api/search" + queryString,
                        RestPageImpl.class);

        //then
        HttpStatus responseStatusCode = responseEntity.getStatusCode();
        RestPageImpl responseBody = responseEntity.getBody();
        assert responseBody != null;
        SearchResponseDto searchResponseDto = (SearchResponseDto) responseBody.getContent().get(0);

        assertEquals(HttpStatus.OK, responseStatusCode);

        assertNotNull(responseBody);
        assertEquals(1L, searchResponseDto.getProductId());
        assertEquals("릴렉스 핏 크루 넥 반팔 티셔츠", searchResponseDto.getName());
        assertEquals("image.png", searchResponseDto.getThumbnail());
        assertEquals("무신사 스탠다드", searchResponseDto.getBrandName());
        assertEquals(10690, searchResponseDto.getPrice());
        assertEquals(100, searchResponseDto.getAmount());
        assertEquals(69058, searchResponseDto.getReviewNum());
        assertEquals(4.8, searchResponseDto.getReviewAvg());
    }

    @Test
    @DisplayName("검색에 성공한 경우 - 필터 적용")
    @Order(2)
    void case1_2() {
        //given
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;
        String keyword = "무탠다드";
        String queryString = "?page=" + page
                + "&sort=" + sort
                + "&categoryFilter=" + categoryFilter
                + "&minPriceFilter=" + minPriceFilter
                + "&maxPriceFilter=" + maxPriceFilter
                + "&reviewFilter=" + reviewFilter
                + "&keyword=" + keyword;

        //when
        ResponseEntity<Page> responseEntity
                = restTemplate.getForEntity(
                        "/api/search" + queryString,
                            Page.class);

        //then
        HttpStatus responseStatusCode = responseEntity.getStatusCode();
        Page<SearchResponseDto> responseBody = responseEntity.getBody();
        SearchResponseDto searchResponseDto = responseBody.getContent().get(0);

        assertEquals(HttpStatus.OK, responseStatusCode);

        assertNotNull(responseBody);
        assertEquals(1L, searchResponseDto.getProductId());
        assertEquals("릴렉스 핏 크루 넥 반팔 티셔츠", searchResponseDto.getName());
        assertEquals("image.png", searchResponseDto.getThumbnail());
        assertEquals("무신사 스탠다드", searchResponseDto.getBrandName());
        assertEquals(10690, searchResponseDto.getPrice());
        assertEquals(100, searchResponseDto.getAmount());
        assertEquals(69058, searchResponseDto.getReviewNum());
        assertEquals(4.8, searchResponseDto.getReviewAvg());
    }

    @Test
    @DisplayName("검색 결과가 존재하지 않는 경우")
    @Order(3)
    void case2() {
        //given
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 0;
        int maxPriceFilter = 0;
        float reviewFilter = 4.5F;
        String keyword = "무탠다드";
        String queryString = "?page=" + page
                + "&sort=" + sort
                + "&categoryFilter=" + categoryFilter
                + "&minPriceFilter=" + minPriceFilter
                + "&maxPriceFilter=" + maxPriceFilter
                + "&reviewFilter=" + reviewFilter
                + "&keyword=" + keyword;

        //when
        ResponseEntity<ExceptionResponseDto> responseEntity
                = restTemplate.getForEntity(
                        "/api/search" + queryString,
                            ExceptionResponseDto.class);

        //then
        HttpStatus responseStatusCode = responseEntity.getStatusCode();
        ExceptionResponseDto responseBody = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatusCode);

        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals("상품이 존재하지 않습니다.", responseBody.getMessage());
    }

    @Test
    @DisplayName("키워드가 공란인 경우")
    @Order(4)
    void case3() {
        //given
        int page = 0;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;
        String keyword = "";
        String queryString = "?page=" + page
                + "&sort=" + sort
                + "&categoryFilter=" + categoryFilter
                + "&minPriceFilter=" + minPriceFilter
                + "&maxPriceFilter=" + maxPriceFilter
                + "&reviewFilter=" + reviewFilter
                + "&keyword=" + keyword;

        //when
        ResponseEntity<ExceptionResponseDto> responseEntity
                = restTemplate.getForEntity(
                        "/api/search" + queryString,
                            ExceptionResponseDto.class);

        //then
        HttpStatus responseStatusCode = responseEntity.getStatusCode();
        ExceptionResponseDto responseBody = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatusCode);

        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals("키워드를 입력해주세요", responseBody.getMessage());
    }

    @Test
    @DisplayName("존재하지 않는 페이지를 요청하는 경우")
    @Order(5)
    void case4() {
        //given
        int page = 100000;
        String sort = "리뷰순";
        String categoryFilter = "상의";
        int minPriceFilter = 10000;
        int maxPriceFilter = 50000;
        float reviewFilter = 4.5F;
        String keyword = "무탠다드";
        String queryString = "?page=" + page
                + "&sort=" + sort
                + "&categoryFilter=" + categoryFilter
                + "&minPriceFilter=" + minPriceFilter
                + "&maxPriceFilter=" + maxPriceFilter
                + "&reviewFilter=" + reviewFilter
                + "&keyword=" + keyword;

        //when
        ResponseEntity<ExceptionResponseDto> responseEntity
                = restTemplate.getForEntity(
                        "/api/search" + queryString,
                            ExceptionResponseDto.class);

        //then
        HttpStatus responseStatusCode = responseEntity.getStatusCode();
        ExceptionResponseDto responseBody = responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseStatusCode);

        assertNotNull(responseBody);
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseBody.getStatus());
        assertEquals("잘못된 접근입니다.", responseBody.getMessage());
    }

    @Getter
    @Builder
    static class RestPageImpl<SearchResponseDto> extends PageImpl<SearchResponseDto> {

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public RestPageImpl(@JsonProperty("content") List<SearchResponseDto> content,
                            @JsonProperty("number") int number,
                            @JsonProperty("size") int size,
                            @JsonProperty("totalElements") Long totalElements,
                            @JsonProperty("pageable") JsonNode pageable,
                            @JsonProperty("last") boolean last,
                            @JsonProperty("totalPages") int totalPages,
                            @JsonProperty("sort") JsonNode sort,
                            @JsonProperty("first") boolean first,
                            @JsonProperty("numberOfElements") int numberOfElements) {

            super(content, PageRequest.of(number, size), totalElements);
        }

        public RestPageImpl(List<SearchResponseDto> content, Pageable pageable, long total) {
            super(content, pageable, total);
        }

        public RestPageImpl(List<SearchResponseDto> content) {
            super(content);
        }

        public RestPageImpl() {
            super(new ArrayList<>());
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class ExceptionResponseDto{
        private LocalDateTime timestamp = LocalDateTime.now();
        private int status;
        private String error;
        private String code;
        private String message;
    }
}
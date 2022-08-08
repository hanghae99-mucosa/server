package com.hanghae99.mocosa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.layer.repository.ProductRepositoryImpl;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ProductIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepositoryImpl productRepository;

    private HttpHeaders headers;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach //각각의 테스트가 실행되기전에 실행 됨 <-> @BeforeAll
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(1)
    @DisplayName("검색에 성공한 경우")
    void test1() throws JsonProcessingException {
        // given
        String queryParam = "page=0&sort='리뷰'&category_filter=&min_price_filter=&max_price_filter=&review_filter=&keyword='무탠다드'";
        HttpEntity<String> request = new HttpEntity<>("", headers);
        // when
        ResponseEntity<ResultDto> response = restTemplate.exchange(
                "/api/search?"+queryParam,
                HttpMethod.GET,
                request,
                ResultDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResultDto resultDto = response.getBody();
        List<ProductResponseDto> content = resultDto.content;

        assertNotNull(resultDto);
        assertEquals(content.size(), 12);
    }

    @Test
    @Order(2)
    @DisplayName("검색 결과가 존재하지 않는 경우")
    void test2() throws JsonProcessingException {
        // given
        String queryParam = "page=0&sort='리뷰'&category_filter=&min_price_filter=1000000&max_price_filter=&review_filter=&keyword='김치'";
        HttpEntity<String> request = new HttpEntity<>("", headers);
        // when
        ResponseEntity<ResultDto> response = restTemplate.exchange(
                "/api/search?"+queryParam,
                HttpMethod.GET,
                request,
                ResultDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResultDto resultDto = response.getBody();

        assertNotNull(resultDto);
        assertEquals(resultDto.msg, "상품이 존재하지 않습니다.");
    }

    @Test
    @Order(3)
    @DisplayName("찾는 키워드가 공란인 경우")
    void test3() throws JsonProcessingException {
        // given
        String queryParam = "page=0&sort='리뷰'&category_filter=&min_price_filter=1000000&max_price_filter=&review_filter=&keyword=";
        HttpEntity<String> request = new HttpEntity<>("", headers);
        // when
        ResponseEntity<ResultDto> response = restTemplate.exchange(
                "/api/search?"+queryParam,
                HttpMethod.GET,
                request,
                ResultDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResultDto resultDto = response.getBody();

        assertNotNull(resultDto);
        assertEquals(resultDto.msg, "키워드를 입력해주세요.");
    }

    @Test
    @Order(4)
    @DisplayName("찾는 키워드가 공란인 경우")
    void test4() throws JsonProcessingException {
        // given
        String queryParam = "page=1000&sort='리뷰'&category_filter=&min_price_filter=1000000&max_price_filter=&review_filter=&keyword=";
        HttpEntity<String> request = new HttpEntity<>("", headers);

        // when
        ResponseEntity<ResultDto> response = restTemplate.exchange(
                "/api/search?"+queryParam,
                HttpMethod.GET,
                request,
                ResultDto.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ResultDto resultDto = response.getBody();

        assertNotNull(resultDto);
        assertEquals(resultDto.msg, "잘못된 접근입니다");
    }

    @Getter
    @Setter
    @Builder
    static class ResultDto {
        String msg;
        private List<ProductResponseDto> content;
    }

    @Getter
    @Setter
    @Builder
    static class ProductResponseDto {
        Long productId;
        String name;
        String thumbnail;
        String brandName;
        int price;
        int amount;
        int reviewNum;
        int reviewAvg;
    }
}

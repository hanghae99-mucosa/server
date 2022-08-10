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
        String queryParam = "page=0&sort=리뷰순&categoryFilter=&minPriceFilter=&maxPriceFilter=&reviewFilter=&keyword=티셔츠";
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
        assertEquals(3, content.size());
    }

    @Test
    @Order(2)
    @DisplayName("검색 결과가 존재하지 않는 경우")
    void test2() throws JsonProcessingException {
        // given
        String queryParam = "page=0&sort=리뷰순&categoryFilter=&minPriceFilter=1000000&maxPriceFilter=&reviewFilter=&keyword=김치";
        HttpEntity<String> request = new HttpEntity<>("", headers);
        // when
        ResponseEntity<ResultDto> response = restTemplate.exchange(
                "/api/search?"+queryParam,
                HttpMethod.GET,
                request,
                ResultDto.class
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResultDto resultDto = response.getBody();

        assertNotNull(resultDto);
        assertEquals("상품이 존재하지 않습니다.", resultDto.message);
    }

    @Test
    @Order(3)
    @DisplayName("찾는 키워드가 공란인 경우")
    void test3() throws JsonProcessingException {
        // given
        String queryParam = "page=0&sort=리뷰순&categoryFilter=&minPriceFilter=1000000&maxPriceFilter=&reviewFilter=&keyword=";
        HttpEntity<String> request = new HttpEntity<>("", headers);
        // when
        ResponseEntity<ResultDto> response = restTemplate.exchange(
                "/api/search?"+queryParam,
                HttpMethod.GET,
                request,
                ResultDto.class
        );

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResultDto resultDto = response.getBody();

        assertNotNull(resultDto);
        assertEquals("키워드를 입력해주세요", resultDto.message);
    }

    @Getter
    @Setter
    @Builder
    static class ResultDto {
        String message;
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

package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.layer.dto.order.OrderRequestDto;
import com.hanghae99.mocosa.layer.dto.order.OrderResponseDto;
import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


import java.nio.charset.Charset;

import static com.hanghae99.mocosa.config.exception.code.ErrorCode.ORDER_NO_STOCK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private ObjectMapper objectMapper;

    /**
     * PRODUCT_ID  	AMOUNT  	NAME  	                            PRICE  	REVIEW_AVG  	REVIEW_NUM  	THUMBNAIL  	BRAND_ID  	CATEGORY_ID
     * 1	        100	        릴렉스 핏 크루 넥 반팔 티셔츠	        10690	4.800000190734863	69058	    image.png	1	        1
     * 2	        0	        K87 워크웨어 포켓 반팔티셔츠 (20colors)	75030	4.5	                590	        image2.png	2	        3
     * 3	        79	        사인 로고 후드 그레이	                25000	4.199999809265137	234	        image3.png	3	        4
     * 4	        50	        DRAWING DENIM PANTS	                37000	3.700000047683716	22233	    image4.png	3	        5
     * 5	        44	        Cargo Pant Dusty Blue	            33400	2.5	                990	        image5.png	3	        6
     * 6	        9999	    럭비 스트라이프 티셔츠 네이비	        13020	3.799999952316284	40	        image6.png	3	        2
     * 7	        100	        릴렉스 핏 크루 넥 긴팔 티셔츠	        20000	3.0	                5000	    image.png	1	        1
     */

    // @PostMapping("/api/products/{productId}")
    @Test
    @DisplayName("Case 1 : 상품 데이터를 정상적으로 가져온 경우 ⇒ 성공")
    public void getProduct() {
        //given
        Long productId = 1L;

        //when
        ResponseEntity<ProductResponseDto> response = restTemplate
                .getForEntity("/api/products/" + productId, ProductResponseDto.class);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductResponseDto body = response.getBody();

        assertThat(body.getName()).isEqualTo("릴렉스 핏 크루 넥 반팔 티셔츠");
        assertThat(body.getPrice()).isEqualTo(10690);
    }

    @Test
    @DisplayName("Case 2 : 존재하지 않는 상품ID를 요청하는 경우 ⇒ 실패")
    public void getExceptionProduct() {
        //given
        Long productId = 8L;

        //when
        ResponseEntity<ErrorResponseDto> response = restTemplate
                .getForEntity("/api/products/" + productId, ErrorResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto body = response.getBody();

        //then
        assertThat(body.getStatus()).isEqualTo(400);
        assertThat(body.getCode()).isEqualTo("DETAIL_NO_PRODUCT");
        assertThat(body.getMessage()).isEqualTo("존재하지 않는 상품입니다.");
    }

    /**
     * @PostMapping("/api/products/{productId}") public String createOrder(@PathVariable Long productId,
     * @RequestBody Integer orderAmount,
     * @RequestBody User userDetails
     * //                             ,@AuthenticationPrincipal UserDetailsImpl userDetails
     */
    @Test
    @DisplayName("Case 1 : 주문에 성공한 경우 ⇒ 성공")
    public void createOrder() {
        //given
        Long productId = 1L;
        OrderRequestDto requestDto = new OrderRequestDto(100);

        //when
        //header 을 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json"));

        //Body 을 설정
        MultiValueMap<String, Integer> body = new LinkedMultiValueMap<String, Integer>();

        //Entity를 설정
        HttpEntity<OrderRequestDto> entity = new HttpEntity<>(requestDto, headers);


        ResponseEntity<OrderResponseDto> response = restTemplate
                .postForEntity("/api/products/" + productId, entity, OrderResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderResponseDto result = response.getBody();

        assertThat(result.getMessage()).isEqualTo("주문에 성공하셨습니다.");
    }

    @Test
    @DisplayName("Case 2 : 재고가 없는 경우 ⇒ 실패")
    public void getExceptionOrder() {
        //given
        Long productId = 1L;
        OrderRequestDto requestDto = new OrderRequestDto(120);

        //when
        //header 을 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application","json"));

        //Body 을 설정
        MultiValueMap<String, Integer> body = new LinkedMultiValueMap<String, Integer>();

        //Entity를 설정
        HttpEntity<OrderRequestDto> entity = new HttpEntity<>(requestDto, headers);


        ResponseEntity<ErrorResponseDto> response = restTemplate
                .postForEntity("/api/products/" + productId, entity, ErrorResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponseDto result = response.getBody();
        assertThat(result.getCode()).isEqualTo("ORDER_NO_STOCK");
        assertThat(result.getMessage()).isEqualTo("재고 수량이 부족합니다.");
    }
}

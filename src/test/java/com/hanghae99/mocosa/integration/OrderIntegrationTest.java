package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import com.hanghae99.mocosa.layer.dto.order.OrderRequestDto;
import com.hanghae99.mocosa.layer.dto.order.OrderResponseDto;
import com.hanghae99.mocosa.layer.dto.product.ProductDetailResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//테스트 환경에서 스프링 동작을 통해 필요한 의존성 제공
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// test 클래스 당 인스턴스 생성
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//Test의 실행 순서를 보장하기 위함
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    private HttpHeaders headers;

    private ObjectMapper mapper = new ObjectMapper();

    private String userToken;

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
    @BeforeEach //각각의 테스트가 실행되기전에 실행 됨 <-> @BeforeAll
    public void setup() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    @Test
    @Order(1)
    @DisplayName("Case 5: 유저 로그인 성공 케이스")
    void TODO_SIGNIN_RESULT_SUCCESS() throws JsonProcessingException {
        UserIntegrationTest.SigninDto userForSignin = UserIntegrationTest.SigninDto.builder()
                .email("test1@test.com")
                .password("abc123123*")
                .build();

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
        userToken = signinResponseDto.getToken();
    }

    // @PostMapping("/api/products/{productId}")
    @Test
    @Order(2)
    @DisplayName("Case 1 : 상품 데이터를 정상적으로 가져온 경우 ⇒ 성공")
    public void getProduct() {
        //given
        Long productId = 1L;
        headers.set("Authorization", userToken);
        HttpEntity<String> entity = new HttpEntity<>("",headers);

        //when
//        ResponseEntity<ProductResponseDto> response = restTemplate
//                .getForEntity("/api/products/" + productId,
//                        ProductResponseDto.class
//                        ,entity);

        ResponseEntity<ProductDetailResponseDto> response = restTemplate.exchange(
                "/products/" + productId,
                HttpMethod.GET,
                entity,
                ProductDetailResponseDto.class
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ProductDetailResponseDto body = response.getBody();

        assertThat(body.getName()).isEqualTo("릴렉스 핏 크루 넥 반팔 티셔츠");
        assertThat(body.getPrice()).isEqualTo(10690);
    }

    @Test
    @Order(3)
    @DisplayName("Case 2 : 존재하지 않는 상품ID를 요청하는 경우 ⇒ 실패")
    public void getExceptionProduct() {
        //given
        Long productId = 9L;
        headers.set("Authorization", userToken);
        HttpEntity<String> entity = new HttpEntity<>("",headers);

        //when
//        ResponseEntity<ErrorResponseDto> response = restTemplate
//                .getForEntity("/api/products/" + productId,ErrorResponseDto.class);
        ResponseEntity<ErrorResponseDto> response = restTemplate.exchange(
                "/products/" + productId,
                HttpMethod.GET,
                entity,
                ErrorResponseDto.class
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto body = response.getBody();

        //then
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
    @Order(4)
    @DisplayName("Case 1 : 주문에 성공한 경우 ⇒ 성공")
    public void createOrder() {
        //given
        Long productId = 1L;
        OrderRequestDto requestDto = new OrderRequestDto(100);

        //when
        headers.set("Authorization", userToken);

        //Entity를 설정
        HttpEntity<OrderRequestDto> entity = new HttpEntity<>(requestDto, headers);


        ResponseEntity<OrderResponseDto> response = restTemplate
                .postForEntity("/products/" + productId, entity, OrderResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        OrderResponseDto result = response.getBody();

        assertThat(result.getMessage()).isEqualTo("주문에 성공하셨습니다.");
    }

    @Test
    @Order(5)
    @DisplayName("Case 2 : 재고가 없는 경우 ⇒ 실패")
    public void getExceptionOrder() {
        //given
        Long productId = 1L;
        OrderRequestDto requestDto = new OrderRequestDto(120);

        //when
        headers.set("Authorization", userToken);
        //Entity를 설정
        HttpEntity<OrderRequestDto> entity = new HttpEntity<>(requestDto, headers);


        ResponseEntity<ErrorResponseDto> response = restTemplate
                .postForEntity("/products/" + productId, entity, ErrorResponseDto.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorResponseDto result = response.getBody();
        assertThat(result.getCode()).isEqualTo("ORDER_NO_STOCK");
        assertThat(result.getMessage()).isEqualTo("재고 수량이 부족합니다.");
    }

    @Getter
    @Setter
    @Builder
    static class loginDto {
        private static String email;
        private static String password;
    }
}

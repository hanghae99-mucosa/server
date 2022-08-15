package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyRequestDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyResponseDto;
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
public class RestockNotificationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;


    private HttpHeaders headers;

    private ObjectMapper mapper = new ObjectMapper();

    private String userToken;

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

    //@PostMapping("/api/notification")
    @Test
    @Order(2)
    @DisplayName("Case 1 : 재입고 알림 등록 신청를 성공한 경우 ⇒ 성공")
    public void TODO_Product_Id를_넘겼을_경우_RESULT_Notify_를_넘긴다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when
        headers.set("Authorization", userToken);

        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/notification", entity, NotifyResponseDto.class);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        NotifyResponseDto body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("재입고 알림 등록을 성공했습니다.");
    }

    @Test
    @Order(3)
    @DisplayName("Case 2 : 재입고 알림 등록 요청이 여러번 들어오는 경우 ⇒ 실패")
    public void TODO_같은_Notify_를_던졌을_경우_RESULT_Exception_를_던진다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when
        headers.set("Authorization", userToken);

        // 첫번째 요청은 성공
        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/notification", entity, NotifyResponseDto.class);


        // 두번째 요청은 실패
        HttpEntity<NotifyRequestDto> error = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ErrorResponseDto> errorResponse = restTemplate
                .postForEntity("/notification", error, ErrorResponseDto.class);

        //then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto result = errorResponse.getBody();

        assertThat(result.getCode()).isEqualTo("ALARM_ALREADY_REQUEST");
        assertThat(result.getMessage()).isEqualTo("이미 처리된 요청입니다.");
    }

    @Test
    @Order(4)
    @DisplayName("Case 1 : 재입고 알림 삭제 신청를 성공한 경우 ⇒ 성공")
    public void TODO_Product_Id를_넘겼을_경우_RESULT_Notify_를_삭제한다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when
        headers.set("Authorization", userToken);

        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);

            //먼저 요청을 등록한다.
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/notification", entity, NotifyResponseDto.class);

            // 그 후 삭제를 요청한다.
        ResponseEntity<NotifyResponseDto> deleteResponse
                = restTemplate.exchange("/notification", HttpMethod.DELETE, entity, NotifyResponseDto.class);


        //then
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        NotifyResponseDto result = deleteResponse.getBody();
        assertThat(result.getMessage()).isEqualTo("재입고 알림 삭제를 성공했습니다.");
    }

    @Test
    @Order(5)
    @DisplayName("Case 2 : 재입고 알림 삭제 신청 요청이 여러번 들어오는 경우 ⇒ 실패")
    public void TODO_여러_개의_Notify_를_던지면_RESULT_Exception_를_던진다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when
        headers.set("Authorization", userToken);

        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);

            //먼저 요청을 등록한다.
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/notification", entity, NotifyResponseDto.class);

            // 그 후 삭제를 요청한다.
        ResponseEntity<NotifyResponseDto> deleteResponse
                = restTemplate.exchange("/notification", HttpMethod.DELETE, entity, NotifyResponseDto.class);

            // 그 후 중복으로 요청을 보낸다.
        ResponseEntity<ErrorResponseDto> errorResponse
                = restTemplate.exchange("/notification", HttpMethod.DELETE, entity, ErrorResponseDto.class);

        //then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto result = errorResponse.getBody();

        assertThat(result.getCode()).isEqualTo("ALARM_ALREADY_REQUEST");
        assertThat(result.getMessage()).isEqualTo("이미 처리된 요청입니다.");
    }
    @Getter
    @Setter
    @Builder
    static class loginDto {
        private static String email;
        private static String password;
    }
}

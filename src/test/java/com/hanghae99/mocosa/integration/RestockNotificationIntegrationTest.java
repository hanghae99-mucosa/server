package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.exception.ErrorResponseDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyRequestDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestockNotificationIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private ObjectMapper objectMapper;

    //@PostMapping("/api/notification")
    @Test
    @DisplayName("Case 1 : 재입고 알림 등록 신청를 성공한 경우 ⇒ 성공")
    public void Todo_Product_Id를_넘겼을_경우_Result_Notify_를_넘긴다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));

        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/api/notification", entity, NotifyResponseDto.class);


        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        NotifyResponseDto body = response.getBody();
        assertThat(body.getMessage()).isEqualTo("재입고 알림 등록을 성공했습니다.");
    }

    @Test
    @DisplayName("Case 2 : 재입고 알림 등록 요청이 여러번 들어오는 경우 ⇒ 실패")
    public void Todo_같은_Notify_를_던졌을_경우_Result_Exception_를_던진다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));

        // 첫번째 요청은 성공
        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/api/notification", entity, NotifyResponseDto.class);


        // 두번째 요청은 실패
        HttpEntity<NotifyRequestDto> error = new HttpEntity<>(requestDto, headers);
        ResponseEntity<ErrorResponseDto> errorResponse = restTemplate
                .postForEntity("/api/notification", error, ErrorResponseDto.class);

        //then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto result = errorResponse.getBody();

        assertThat(result.getCode()).isEqualTo("ALARM_ALREADY_REQUEST");
        assertThat(result.getMessage()).isEqualTo("이미 처리된 요청입니다.");
    }

    @Test
    @DisplayName("Case 1 : 재입고 알림 삭제 신청를 성공한 경우 ⇒ 성공")
    public void Todo_Product_Id를_넘겼을_경우_Result_Notify_를_삭제한다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));

        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);

            //먼저 요청을 등록한다.
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/api/notification", entity, NotifyResponseDto.class);

            // 그 후 삭제를 요청한다.
        ResponseEntity<NotifyResponseDto> deleteResponse
                = restTemplate.exchange("/api/notification", HttpMethod.DELETE, entity, NotifyResponseDto.class);


        //then
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        NotifyResponseDto result = deleteResponse.getBody();
        assertThat(result.getMessage()).isEqualTo("재입고 알림 삭제를 성공했습니다.");
    }

    @Test
    @DisplayName("Case 2 : 재입고 알림 삭제 신청 요청이 여러번 들어오는 경우 ⇒ 실패")
    public void Todo_여러_개의_Notify_를_던지면_Result_Exception_를_던진다() {
        //given
        NotifyRequestDto requestDto = new NotifyRequestDto(1L);

        //when.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));

        HttpEntity<NotifyRequestDto> entity = new HttpEntity<>(requestDto, headers);

            //먼저 요청을 등록한다.
        ResponseEntity<NotifyResponseDto> response = restTemplate
                .postForEntity("/api/notification", entity, NotifyResponseDto.class);

            // 그 후 삭제를 요청한다.
        ResponseEntity<NotifyResponseDto> deleteResponse
                = restTemplate.exchange("/api/notification", HttpMethod.DELETE, entity, NotifyResponseDto.class);

            // 그 후 중복으로 요청을 보낸다.
        ResponseEntity<ErrorResponseDto> errorResponse
                = restTemplate.exchange("/api/notification", HttpMethod.DELETE, entity, ErrorResponseDto.class);

        //then
        assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorResponseDto result = errorResponse.getBody();

        assertThat(result.getCode()).isEqualTo("ALARM_ALREADY_REQUEST");
        assertThat(result.getMessage()).isEqualTo("이미 처리된 요청입니다.");
    }
}

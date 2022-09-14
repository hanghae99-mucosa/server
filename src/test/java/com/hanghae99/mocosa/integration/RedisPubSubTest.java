package com.hanghae99.mocosa.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// test 클래스 당 인스턴스 생성
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//Test의 실행 순서를 보장하기 위함
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class RedisPubSubTest {

    @Autowired
    private TestRestTemplate restTemplate;


    private HttpHeaders headers;

    private ObjectMapper mapper = new ObjectMapper();

    private String userToken;

    UserIntegrationTest.SigninDto userForSignin = UserIntegrationTest.SigninDto.builder()
            .email("test1@test.test")
            .password("test123!")
            .build();

//    @BeforeEach //각각의 테스트가 실행되기전에 실행 됨 <-> @BeforeAll
//    public void setup() {
//        headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//    }

    @Test
    @Order(1)
    @DisplayName("Case 5: 유저 로그인 성공 케이스")
    void TODO_SIGNIN_RESULT_SUCCESS() throws JsonProcessingException {

        String requestBody = mapper.writeValueAsString(userForSignin);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
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

    @Test
    @Order(2)
    @DisplayName("유저가 알림을 받는 환경을 구성")
    void TODO_SUB_USER_AND_SEND_MESSAGE_RESULT_LOG() {
        //given
        headers = new HttpHeaders();
        headers.set("Authorization", userToken);
        HttpEntity<String> request = new HttpEntity<>("", headers);

        String message = "성공함?";

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .path("/pubsub/room/" + userForSignin.getEmail())
                .queryParam("name", userForSignin.getEmail())
                .queryParam("message", message)
                .build();

        // when
        restTemplate.put(
                "/pubsub/room/" + userForSignin.getEmail(),
                request
        );

        ResponseEntity<Void> exchange = restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.POST,
                request,
                Void.class
        );

    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public class RoomMessage implements Serializable {
        private static final long serialVersionUID = 2082503192322391880L;
        private String roomId;
        private String name;
        private String message;
    }

    @Service
    public class RedisPublisher {
        @Autowired
        private RedisTemplate<String, Object> redisTemplate;

        public void publish(ChannelTopic topic, RoomMessage message) {
            redisTemplate.convertAndSend(topic.getTopic(), message);
        }
    }

    @RequiredArgsConstructor
    @Service
    public class RedisSubscriber implements MessageListener {

        private final ObjectMapper objectMapper;
        private final RedisTemplate redisTemplate;

        @Override
        public void onMessage(Message message, byte[] pattern) {
            try {
                String body = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
                RoomMessage roomMessage = objectMapper.readValue(body, RoomMessage.class);
                log.info("Room - Message : {}", roomMessage.toString());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @RequiredArgsConstructor
    @RequestMapping("/pubsub")
    @RestController
    public class PubSubController {
        // topic에 메시지 발행을 기다리는 Listner
        private final RedisMessageListenerContainer redisMessageListener;
        // 발행자
        private final RedisPublisher redisPublisher;
        // 구독자
        private final RedisSubscriber redisSubscriber;
        // topic 이름으로 topic정보를 가져와 메시지를 발송할 수 있도록 Map에 저장
        private Map<String, ChannelTopic> channels;

        @PostConstruct
        public void init() {
            // topic 정보를 담을 Map을 초기화
            channels = new HashMap<>();
        }

        // 유효한 Topic 리스트 반환
        @GetMapping("/room")
        public Set<String> findAllRoom() {
            return channels.keySet();
        }

        // 신규 Topic을 생성하고 Listener등록 및 Topic Map에 저장
        @PutMapping("/room/{roomId}")
        public void createRoom(@PathVariable String roomId) {
            ChannelTopic channel = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, channel);
            channels.put(roomId, channel);
        }

        // 특정 Topic에 메시지 발행
        @PostMapping("/room/{roomId}")
        public void pushMessage(@PathVariable String roomId, @RequestParam String name, @RequestParam String message) {
            ChannelTopic channel = channels.get(roomId);
            RoomMessage roomMessage = new RoomMessage(roomId, name, message);
            redisPublisher.publish(channel, roomMessage);
        }

        // Topic 삭제 후 Listener 해제, Topic Map에서 삭제
        @DeleteMapping("/room/{roomId}")
        public void deleteRoom(@PathVariable String roomId) {
            ChannelTopic channel = channels.get(roomId);
            redisMessageListener.removeMessageListener(redisSubscriber, channel);
            channels.remove(roomId);
        }
    }
}

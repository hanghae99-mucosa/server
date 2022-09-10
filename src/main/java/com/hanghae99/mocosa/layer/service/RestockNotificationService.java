package com.hanghae99.mocosa.layer.service;


import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.AlarmException;
import com.hanghae99.mocosa.layer.dto.notify.NotifyRequestDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyResponseDto;
import com.hanghae99.mocosa.layer.dto.notify.RestockNotificationResponseDto;
import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.RestockNotification;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.EmitterRepository;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.RestockNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import javax.persistence.EntityNotFoundException;
import java.beans.Transient;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestockNotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final RestockNotificationRepository restockNotificationRepository;
    private final ProductRepository productRepository;
    private final EmitterRepository emitterRepository;

    //MainMethod
    @Transient
    public NotifyResponseDto createNotify(NotifyRequestDto notifyRequestDto, User userDetails) {

        Product product = findProduct(notifyRequestDto);

        NotifyResponseDto result = createNotify(userDetails, product);

        return result;
    }

    //MainMethod
    @Transient
    public NotifyResponseDto deleteNotify(NotifyRequestDto notifyRequestDto, User userDetails) {

        Product product = findProduct(notifyRequestDto);

        NotifyResponseDto result = deleteNotify(userDetails, product);
        return result;
    }

    private NotifyResponseDto createNotify(User userDetails, Product product) {

        Optional<RestockNotification> notification =
                restockNotificationRepository
                        .findRestockNotificationByUserAndProduct(userDetails, product);
        if (notification.isPresent()) {
            throw new AlarmException(ErrorCode.ALARM_ALREADY_REQUEST);
        }
        RestockNotification restockNotification = RestockNotification.builder()
                .product(product)
                .user(userDetails)
                .build();

        restockNotificationRepository.save(restockNotification);

        return new NotifyResponseDto("재입고 알림 등록을 성공했습니다.");
    }

    private Product findProduct(NotifyRequestDto notifyRequestDto) {
        Optional<Product> optionalProduct = productRepository.findById(notifyRequestDto.getProductId());
        if (optionalProduct.isEmpty()) {
            throw new AlarmException(ErrorCode.ALARM_ETC);
        }
        Product product = optionalProduct.orElseThrow(() -> new AlarmException(ErrorCode.ALARM_ETC));
        return product;
    }

    private NotifyResponseDto deleteNotify(User userDetails, Product product) {
        Optional<RestockNotification> notification =
                restockNotificationRepository
                        .findRestockNotificationByUserAndProduct(userDetails, product);

        if (notification.isEmpty()) {
            throw new AlarmException(ErrorCode.ALARM_ALREADY_REQUEST);
        }

        RestockNotification restockNotification = notification.orElseThrow(() -> new AlarmException(ErrorCode.ALARM_ETC));
        restockNotificationRepository.deleteById(restockNotification.getRestockId());
        return new NotifyResponseDto("재입고 알림 삭제를 성공했습니다.");
    }

        // 1
    public SseEmitter subscribe(User user) {
        String id = user.getEmail();

        // 2
        emitterRepository.deleteById(id);
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송
        sendToClient(emitter, id, "EventStream Created. [email=" + id + "]");

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        List<RestockNotification> restockList = restockNotificationRepository.findAllByUserAndAlarmFlag(user, true);
        restockList.forEach(this::send);

        return emitter;
    }

    // 3
    private void sendToClient(SseEmitter emitter, String id, String data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
        }
    }
    private void sendToClient(SseEmitter emitter, String id, RestockNotification data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(RestockNotificationResponseDto.from(data)));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
        }
    }

    @Transactional
    public void checkAlarmFlag(RestockNotification notification) {
        if(notification.getAlarmFlag()==false){
            notification.checkAlarm();
            restockNotificationRepository.save(notification);
        }
    }

    public void send(RestockNotification restockNotification) {
        String id = restockNotification.getUserEmail();

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        SseEmitter sseEmitter = emitterRepository.findById(id);
        if(sseEmitter!=null){
            sendToClient(sseEmitter, id, restockNotification);
        }

//        checkAlarmFlag(restockNotification);
    }
}

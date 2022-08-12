package com.hanghae99.mocosa.layer.service;


import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.AlarmException;
import com.hanghae99.mocosa.layer.dto.notify.NotifyRequestDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyResponseDto;
import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.RestockNotification;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.RestockNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestockNotificationService {
    private final RestockNotificationRepository restockNotificationRepository;
    private final ProductRepository productRepository;

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
        Optional<RestockNotification> optionalRestockNotification = restockNotificationRepository
                .findRestockNotificationByUser_UserIdAndProduct_ProductId(userDetails.getUserId(), product.getProductId());

        if (optionalRestockNotification.isPresent()) {
            throw new AlarmException(ErrorCode.ALARM_ALREADY_REQUEST);
        }

        RestockNotification restockNotification = RestockNotification.builder()
                .product(product)
                .user(userDetails)
                .alarmFlag(false)
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
        Optional<RestockNotification> optionalRestockNotification = restockNotificationRepository
                .findRestockNotificationByUser_UserIdAndProduct_ProductId(product.getProductId(), userDetails.getUserId());

        if (optionalRestockNotification.isEmpty()) {
            throw new AlarmException(ErrorCode.ALARM_ALREADY_REQUEST);
        }

        RestockNotification restockNotification = optionalRestockNotification.orElseThrow(() -> new AlarmException(ErrorCode.ALARM_ETC));
        restockNotificationRepository.deleteById(restockNotification.getRestockId());
        return new NotifyResponseDto("재입고 알림 삭제를 성공했습니다.");
    }
}

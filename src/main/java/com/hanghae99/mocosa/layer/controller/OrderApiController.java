package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.order.OrderHistoryResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    @GetMapping("/api/users/orders")
    public ResponseEntity<Page<OrderHistoryResponseDto>> getOrderHistory(@AuthenticationPrincipal User userDetails, @RequestParam int page) {

        Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage = orderService.getOrderHistory(userDetails, page);

        return new ResponseEntity<>(orderHistoryResponseDtoPage, HttpStatus.OK);
    }

//    @GetMapping("/api/users/orders")
//    public ResponseEntity<Page<OrderHistoryResponseDto>> getOrderHistory(@RequestParam int page) {
//
//        Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage = orderService.getOrderHistory(page);
//
//        return new ResponseEntity<>(orderHistoryResponseDtoPage, HttpStatus.OK);
//    }
}

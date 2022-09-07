package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.MyPageException;
import com.hanghae99.mocosa.config.exception.custom.OrderException;
import com.hanghae99.mocosa.layer.dto.order.OrderCancelResponseDto;
import com.hanghae99.mocosa.layer.dto.order.OrderHistoryResponseDto;
import com.hanghae99.mocosa.layer.model.Order;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final static int PAGEABLE_SIZE = 10;
    private final static Direction PAGEABLE_DIRECTION = Direction.DESC;
    private final static String PAGEABLE_SORT = "createdAt";

    public Page<OrderHistoryResponseDto> getOrderHistory(UserDetailsImpl userDetails, int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page-1, PAGEABLE_SIZE, PAGEABLE_DIRECTION, PAGEABLE_SORT);

        Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage = orderRepository.findByUser(user, pageable);

        validateNoOrderHistory(orderHistoryResponseDtoPage);

        validatePage(orderHistoryResponseDtoPage, page);

        return orderHistoryResponseDtoPage;
    }

    private void validateNoOrderHistory(Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage) {
        long totalElements = orderHistoryResponseDtoPage.getTotalElements();

        if(totalElements == 0) {
            throw new MyPageException(ErrorCode.MYPAGE_NO_DATA);
        }
    }

    private void validatePage(Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage, int page) {
        int totalPage = orderHistoryResponseDtoPage.getTotalPages();

        if(totalPage < page) {
            throw new MyPageException(ErrorCode.MYPAGE_NO_PAGE);
        }
    }

    public OrderCancelResponseDto cancelOrder(Long orderId, UserDetailsImpl userDetails) {
        if(!orderRepository.findById(orderId).isPresent()) {
            throw new OrderException(ErrorCode.ORDERCANCEL_NO_ORDER);
        }

        String consumer = orderRepository.findById(orderId).get().getUser().getEmail();
        String currentUser = userDetails.getUser().getEmail();

        if(!consumer.equals(currentUser)) {
            throw new OrderException(ErrorCode.ORDERCANCEL_BAD_REQUEST);
        }

        orderRepository.deleteById(orderId);

        return new OrderCancelResponseDto("주문이 취소되었습니다");
    }
}

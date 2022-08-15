package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.MyPageException;
import com.hanghae99.mocosa.layer.dto.order.OrderHistoryResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.OrderRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepositoryImpl orderRepositoryImpl;

    private final static int PAGEABLE_SIZE = 10;
    private final static Direction PAGEABLE_DIRECTION = Direction.DESC;
    private final static String PAGEABLE_SORT = "createdAt";

    public Page<OrderHistoryResponseDto> getOrderHistory(UserDetailsImpl userDetails, int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGEABLE_SIZE, PAGEABLE_DIRECTION, PAGEABLE_SORT);

        Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage = orderRepositoryImpl.findByUser(user, pageable);

        validatePage(orderHistoryResponseDtoPage, page);

        validateNoOrderHistory(orderHistoryResponseDtoPage);

        return orderHistoryResponseDtoPage;
    }

//    public Page<OrderHistoryResponseDto> getOrderHistory(int page) {
//
//        User user = new User(4L, "test4@test.com", "1234");
//        Pageable pageable = PageRequest.of(page, PAGEABLE_SIZE, PAGEABLE_DIRECTION, PAGEABLE_SORT);
//
//        Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage = orderRepositoryImpl.findByUser(user, pageable);
//
//        validatePage(orderHistoryResponseDtoPage, page);
//
//        validateNoOrderHistory(orderHistoryResponseDtoPage);
//
//        return orderHistoryResponseDtoPage;
//    }

    private void validatePage(Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage, int page) {
        int toalPage = orderHistoryResponseDtoPage.getTotalPages();

        if(toalPage < page) {
            throw new MyPageException(ErrorCode.MYPAGE_NO_PAGE);
        }
    }

    private void validateNoOrderHistory(Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage) {
        long totalElements = orderHistoryResponseDtoPage.getTotalElements();

        if(totalElements == 0) {
            throw new MyPageException(ErrorCode.MYPAGE_NO_DATA);
        }
    }
}

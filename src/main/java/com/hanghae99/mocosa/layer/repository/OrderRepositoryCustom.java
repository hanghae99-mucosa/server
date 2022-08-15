package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.order.OrderHistoryResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<OrderHistoryResponseDto> findByUser(User user, Pageable pageable);
}

package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.RestockNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestockNotificationRepository extends JpaRepository<RestockNotification, Long> {
    Optional<RestockNotification> findRestockNotificationByUser_UserIdAndProduct_ProductId(Long userId, Long productId);

}

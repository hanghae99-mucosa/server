package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.RestockNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RestockNotificationRepository extends JpaRepository<RestockNotification, Long> {
    Optional<RestockNotification> findRestockNotificationByUser_UserId(Long userId);
    Optional<RestockNotification> findRestockNotificationByProduct_ProductId(Long productId);
}

package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.RestockNotification;
import com.hanghae99.mocosa.layer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestockNotificationRepository extends JpaRepository<RestockNotification, Long> {

//    @Query("select rn from RestockNotification rn join fetch rn.user u where u.userId = :userId")
//    Optional<RestockNotification> findRestockNotificationByUser_UserId(@Param("userId") Long userId);
//    @Query("select rn from RestockNotification rn join fetch rn.product p where p.productId = :productId")
//    Optional<RestockNotification> findRestockNotificationByProduct_ProductId(@Param("productId")Long productId);
//    Optional<RestockNotification> findRestockNotificationByUser_UserIdAndProduct_ProductId(Long userId, Long productId);
    Optional<RestockNotification> findRestockNotificationByUserAndProduct(User user, Product product);

    List<RestockNotification> findAllByProduct(Product product);

    List<RestockNotification> findAllByUserAndAlarmFlag(User user, Boolean alarmFlag);
}

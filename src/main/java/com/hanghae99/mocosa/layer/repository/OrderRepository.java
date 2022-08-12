package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.Category;
import com.hanghae99.mocosa.layer.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.order.OrderHistoryResponseDto;
import com.hanghae99.mocosa.layer.model.Order;
import com.hanghae99.mocosa.layer.model.QOrder;
import com.hanghae99.mocosa.layer.model.User;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QOrder order = QOrder.order;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Order.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<OrderHistoryResponseDto> findByUser(User user, Pageable pageable) {
        JPQLQuery<OrderHistoryResponseDto> query = queryFactory.select(Projections.fields(
                OrderHistoryResponseDto.class,
                order.orderId,
                order.createdAt,
                order.product.name.as("productName"),
                order.product.thumbnail,
                order.product.brand.name.as("brandName"),
                order.product.category.category,
                order.product.price,
                order.amount.as("orderAmount"),
                order.totalPrice
        ))
                .from(order)
                .where(order.user.userId.eq(user.getUserId()))
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        QueryResults<OrderHistoryResponseDto> orderHistoryResponseDtoList = query.fetchResults();

        return new PageImpl<>(orderHistoryResponseDtoList.getResults(), pageable, orderHistoryResponseDtoList.getTotal());
    }
}

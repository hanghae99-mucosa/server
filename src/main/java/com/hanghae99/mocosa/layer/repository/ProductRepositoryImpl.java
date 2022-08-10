package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static com.hanghae99.mocosa.layer.model.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;
    private final JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    @Override
    public PageImpl<ProductResponseDto> searchItems(Pageable pageable, String categoryFilter, int minPriceFilter, int maxPriceFilter, int reviewFilter, String keyWord) {
        // 검증된 키워드로 검색을 하고 그 결과인 QueryResults<ProductResponseDto> 를 리턴 해주는 메소드 
        QueryResults<ProductResponseDto> orderReview = result(pageable, categoryFilter, minPriceFilter, maxPriceFilter, reviewFilter, keyWord);
        // QueryResults<ProductResponseDto> 를 통해서 검증과정을 밟는 메소드

        return new PageImpl<>(orderReview.getResults(), pageable, orderReview.getTotal());
    }


    private QueryResults<ProductResponseDto> result(Pageable pageable, String categoryFilter, int minPriceFilter, int maxPriceFilter, int reviewFilter, String keyWord) {
        // 전체 조회
        QueryResults<ProductResponseDto> orderReview = queryFactory
                .select(Projections.constructor(ProductResponseDto.class,
                        product.product_id,
                        product.name,
                        product.thumbnail,
                        product.brand.name.as("brandName"),
                        product.category.category.as("category"),
                        product.price,
                        product.amount,
                        product.reviewNum,
                        product.reviewAvg
                ))
                .from(product)
                .where(
                        eqCategory(categoryFilter),
                        inPrice(minPriceFilter, maxPriceFilter),
                        overReviewAvg(reviewFilter)
                )
                .where(
                        product.name.contains(keyWord)
                                .or(product.brand.name.contains(keyWord))
                )
                .orderBy(selectOrder(pageable))
                .offset(pageable.getOffset())
                .limit(12)
                .fetchResults();
        return orderReview;
    }

    private BooleanExpression overReviewAvg(Integer reviewAvg) {

        if (ObjectUtils.isEmpty(reviewAvg)) {
            return null;
        }
        if (reviewAvg >= 5 || reviewAvg < 0) {
            return null;
        }
        return product.price.goe(reviewAvg);
    }

    private BooleanExpression eqCategory(String categoryFilter) {
        if (ObjectUtils.isEmpty(categoryFilter)) {
            return null;
        }
        return product.category.category.eq(categoryFilter);
    }

    private BooleanExpression inPrice(Integer minPrice, Integer maxPrice) {
        if (ObjectUtils.isEmpty(minPrice)) {
            return null;
        }
        if (ObjectUtils.isEmpty(maxPrice)) {
            return null;
        }
        return product.price.goe(minPrice)
                .and(product.price.loe(maxPrice));
    }

    private OrderSpecifier<?> selectOrder(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()) {
                    case "리뷰순":
                        return product.reviewNum.desc();
                    case "고가순":
                        return product.price.desc();
                    case "저가순":
                        return product.price.asc();
                }
            }
        }
        return product.reviewNum.desc();
    }
}

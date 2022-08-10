package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.QSearchResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.QProduct;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QProduct product = QProduct.product;

    @Override
    public List<SearchResponseDto> findAllByNameAndBrandName(Pageable pageable,
                                                             String keyword,
                                                             String categoryFilter,
                                                             Integer minPriceFilter, Integer maxPriceFilter,
                                                             Float reviewFilter) {

        Long lastProductId = (long) (pageable.getPageNumber() - 1) * pageable.getPageSize();
        Sort sort = pageable.getSort();
        int size = pageable.getPageSize();

        return queryFactory
                .select(new QSearchResponseDto(
                        product.product_id.as("productId"),
                        product.name,
                        product.thumbnail,
                        product.brand.name.as("brandName"),
                        product.category.category,
                        product.price,
                        product.amount,
                        product.reviewNum,
                        product.reviewAvg
                ))
                .from(product)
                .where(product.name.contains(keyword).or(product.brand.name.contains(keyword))
//                        .and(product.category.category.eq(categoryFilter))
                        .and(categoryFilter(categoryFilter))
                        .and(product.price.between(minPriceFilter, maxPriceFilter))
                        .and(product.reviewAvg.goe(reviewFilter)))
//                        .and(productIdLt(lastProductId)))
                .orderBy(sortType(sort))
                .offset(pageable.getOffset())
                .limit(size)
                .fetch();

    }

//    private BooleanExpression productIdLt(Long lastProductId) {
//        return product.product_id.lt(lastProductId);
//    }

    private BooleanExpression categoryFilter(String category) {
        return (category == null) || (category.isBlank()) ? null : product.category.category.eq(category);
    }

    private OrderSpecifier<Integer> sortType(Sort sort) {
        if (Sort.by("price").ascending().equals(sort)) {
            return product.price.asc();
        }

        if (Sort.by("price").descending().equals(sort)) {
            return product.price.desc();
        }

        return product.reviewNum.desc();
    }
}

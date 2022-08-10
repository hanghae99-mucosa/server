package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.QProduct;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QProduct product = QProduct.product;

    @Override
    public Page<SearchResponseDto> findAllByKeywordAndFilter(Pageable pageable, String sort, String categoryFilter, int minPriceFilter, int maxPriceFilter, float reviewFilter, String keyword) {
        List<SearchResponseDto> returnSearch = queryFactory.select(Projections.fields(
                        SearchResponseDto.class,
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
                .where(categoryContains(categoryFilter))
                .where(product.price.between(minPriceFilter, maxPriceFilter))
                .where(product.name.contains(keyword).or(product.brand.name.contains(keyword)))
                .where(product.reviewAvg.goe(reviewFilter))
                .orderBy(sortContains(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(returnSearch, pageable, returnSearch.size());
    }

    private BooleanExpression categoryContains(String categoryFilter) {
        return categoryFilter.equals("전체") ? null : product.category.category.eq(categoryFilter);
    }

    private OrderSpecifier<Integer> sortContains(String sort) {
        if(sort.equals("리뷰순")){
            return product.reviewNum.desc();
        }
        if(sort.equals("고가순")){
            return product.price.desc();
        }
        if(sort.equals("저가순")){
            return product.price.asc();
        }
        throw new SearchException(ErrorCode.SING_ECT);
    }
}

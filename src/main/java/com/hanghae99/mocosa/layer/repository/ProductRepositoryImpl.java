package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private static final int PAGEABLE_SIZE = 12;
    private final JPAQueryFactory queryFactory;

    QProduct product = QProduct.product;

    @Override
    public Page<SearchResponseDto> findBySearchRequestDto(SearchRequestDto searchRequestDto, Pageable pageable) {
        List<SearchResponseDto> returnPost = queryFactory.select(Projections.fields(
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
                .where(
                        keywordContains(searchRequestDto.getKeyword()),
                        categoryEq(searchRequestDto.getCategoryFilter()),
                        priceBetween(searchRequestDto.getMinPriceFilter(),searchRequestDto.getMaxPriceFilter()),
                        reviewAvgGt(searchRequestDto.getReviewFilter())
                )
                .orderBy(orderBySort(searchRequestDto.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return new PageImpl<>(returnPost, pageable, returnPost.size());
    }

    private BooleanBuilder keywordContains(String keyword) {
        if (keyword.equals("")){
            throw new SearchException(ErrorCode.SEARCH_BLANK_KEYWORD);
        }
        BooleanBuilder builder = new BooleanBuilder();
        builder
                .or(product.name.contains(keyword))
                .or(product.brand.name.contains(keyword));
        return builder;
    }

    private BooleanExpression reviewAvgGt(Float reviewAvg) {
        return reviewAvg==null ? null : product.reviewAvg.gt(reviewAvg);
    }

    private BooleanExpression priceBetween(Integer minPriceFilter, Integer maxPriceFilter) {
        if (minPriceFilter == null && maxPriceFilter==null) {
            return null;
        }
        return product.price.between(minPriceFilter, maxPriceFilter);
    }

    private BooleanExpression categoryEq(String category) {
        return category.equals("") ? null : product.category.category.eq(category);
    }

    private OrderSpecifier orderBySort(String sort){
        if (sort.equals("저가순")){
            return product.price.asc();
        }
        if (sort.equals("고가순")){
            return product.price.desc();
        }
        if (sort.equals("리뷰순") || sort.equals("")){
            return product.reviewAvg.desc();
        }
        //ErrorCode에 SEARCH_ETC로 해야할듯함
        throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
    }
}

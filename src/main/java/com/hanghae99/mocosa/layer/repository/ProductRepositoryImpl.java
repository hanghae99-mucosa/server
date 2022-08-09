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
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QProduct product = QProduct.product;

    @Override
    public Slice<SearchResponseDto> findBySearchRequestDto(SearchRequestDto searchRequestDto, Pageable pageable) {
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
        return new SliceImpl<>(returnPost, pageable, returnPost.iterator().hasNext());
    }

    private BooleanBuilder keywordContains(String keyword) {
        if (keyword == null){
            return null;
        }
        BooleanBuilder builder = new BooleanBuilder();
        builder
                .and(product.name.contains(keyword))
                .and(product.brand.name.contains(keyword));
        return builder;
    }

    private BooleanExpression reviewAvgGt(Float reviewAvg) {
        return product.reviewAvg.gt(reviewAvg);
    }

    private BooleanExpression priceBetween(Integer minPriceFilter, Integer maxPriceFilter) {
        if (minPriceFilter == null && maxPriceFilter==null) {
            return null;
        }
        return product.price.between(minPriceFilter, maxPriceFilter);
    }

    private BooleanExpression categoryEq(String category) {
        return category==null ? null : product.category.category.eq(category);
    }

    private OrderSpecifier orderBySort(String sort){
        if (sort.equals("저가순")){
            product.price.asc();
        }
        if (sort.equals("고가순")){
            product.price.desc();
        }
        if (sort.equals("리뷰순") || sort==null){
            product.reviewAvg.desc();
        }
        //ErrorCode에 SEARCH_ETC로 해야할듯함
        throw new SearchException(ErrorCode.SEARCH_NO_PAGE);

    }
}

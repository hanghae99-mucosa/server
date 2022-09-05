package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.RestockListResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.*;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.description.method.ParameterList;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    QProduct product = QProduct.product;
    QBrand brand = QBrand.brand;
    QCategory parentCategory = QCategory.category1;

    @Override
    public Page<SearchResponseDto> findBySearchRequestDto(SearchRequestDto searchRequestDto, Pageable pageable) {
        List<Long> productIdList = new ArrayList<>();
        long totalCount = 0;

        if(searchRequestDto.getSearchType().equals("상품명")) {
            productIdList = getProductIdList_productName(searchRequestDto, pageable);
            totalCount = getTotalCount_productName(searchRequestDto);
        }

        if(searchRequestDto.getSearchType().equals("브랜드명")) {
            List<Long> brandIdList = getBrandIdList(searchRequestDto);

            productIdList = getProductIdList_brandName(brandIdList, searchRequestDto, pageable);
            totalCount = getTotalCount_brandName(brandIdList, searchRequestDto);
        }

        if(productIdList.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, totalCount);
        }

        List<SearchResponseDto> returnPost = queryFactory
                                                .select(Projections.fields(
                                                        SearchResponseDto.class,
                                                        product.productId,
                                                        product.name,
                                                        product.thumbnail,
                                                        brand.name.as("brandName"),
                                                        parentCategory.category,
                                                        product.price,
                                                        product.amount,
                                                        product.reviewNum,
                                                        product.reviewAvg
                                                    ))
                                                .from(product)
                                                .innerJoin(brand).on(product.brand.brandId.eq(brand.brandId))
                                                .innerJoin(parentCategory).on(product.category.parentCategory.eq(parentCategory.categoryId))
                                                .where(product.productId.in(productIdList))
                                                .orderBy(orderBySort(searchRequestDto.getSort()))
                                                .fetch();

        return new PageImpl<>(returnPost, pageable,totalCount);
    }

    private List<Long> getBrandIdList(SearchRequestDto searchRequestDto) {
        return queryFactory
                .select(brand.brandId)
                .from(brand)
                .where(brand.name.contains(searchRequestDto.getKeyword()))
                .fetch();
    }

    private List<Long> getProductIdList_productName(SearchRequestDto searchRequestDto, Pageable pageable) {
        return queryFactory
                    .select(product.productId)
                    .from(product)
                    .innerJoin(parentCategory).on(product.category.parentCategory.eq(parentCategory.categoryId))
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
    }

    private List<Long> getProductIdList_brandName(List<Long> brandIdList, SearchRequestDto searchRequestDto, Pageable pageable) {
        return queryFactory
                    .select(product.productId)
                    .from(product)
                    .innerJoin(parentCategory).on(product.category.parentCategory.eq(parentCategory.categoryId))
                    .where(
                            product.brand.brandId.in(brandIdList),
                            categoryEq(searchRequestDto.getCategoryFilter()),
                            priceBetween(searchRequestDto.getMinPriceFilter(),searchRequestDto.getMaxPriceFilter()),
                            reviewAvgGt(searchRequestDto.getReviewFilter())
                    )
                    .orderBy(orderBySort(searchRequestDto.getSort()))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();
    }

    private long getTotalCount_productName(SearchRequestDto searchRequestDto) {
        JPQLQuery<Product> countQuery = queryFactory
                                            .selectFrom(product)
                                            .innerJoin(parentCategory).on(product.category.parentCategory.eq(parentCategory.categoryId))
                                            .where(
                                                    keywordContains(searchRequestDto.getKeyword()),
                                                    categoryEq(searchRequestDto.getCategoryFilter()),
                                                    priceBetween(searchRequestDto.getMinPriceFilter(),searchRequestDto.getMaxPriceFilter()),
                                                    reviewAvgGt(searchRequestDto.getReviewFilter())
                                            );

        return countQuery.fetchCount();
    }

    private long getTotalCount_brandName(List<Long> brandIdList, SearchRequestDto searchRequestDto) {
        JPQLQuery<Product> countQuery = queryFactory
                                            .selectFrom(product)
                                            .innerJoin(parentCategory).on(product.category.parentCategory.eq(parentCategory.categoryId))
                                            .where(
                                                    product.brand.brandId.in(brandIdList),
                                                    categoryEq(searchRequestDto.getCategoryFilter()),
                                                    priceBetween(searchRequestDto.getMinPriceFilter(),searchRequestDto.getMaxPriceFilter()),
                                                    reviewAvgGt(searchRequestDto.getReviewFilter())
                                            );

        return countQuery.fetchCount();
    }

    private BooleanExpression keywordContains(String keyword) {
        return keyword.equals("ALL") ? null : product.name.contains(keyword);
    }

    private BooleanExpression categoryEq(String inputCategory) {
        return inputCategory == null ? null : parentCategory.category.eq(inputCategory);
    }

    private BooleanExpression reviewAvgGt(Float reviewAvg) {
        return reviewAvg == null ? null : product.reviewAvg.goe(reviewAvg);
    }

    private BooleanExpression priceBetween(Integer minPriceFilter, Integer maxPriceFilter) {
        if (minPriceFilter == null && maxPriceFilter==null) {
            return null;
        }
        return product.price.between(minPriceFilter, maxPriceFilter);
    }

    private OrderSpecifier<Integer> orderBySort(String sort){
        if (sort.equals("저가순")){
            return product.price.asc();
        }

        if (sort.equals("고가순")){
            return product.price.desc();
        }

        // default 정렬조건 "리뷰순"
        return product.reviewNum.desc();
    }

    @Override
    public List<RestockListResponseDto> findBySeller(User user) {
        return queryFactory.select(Projections.fields(
                RestockListResponseDto.class,
                product.productId,
                product.name.as("productName")
        ))
                .from(product)
                .where(
                        product.brand.user.userId.eq(user.getUserId()),
                        product.amount.eq(0)
                )
                .fetch();
    }
}
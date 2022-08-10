package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.hanghae99.mocosa.layer.repository.ProductRepositoryImpl;
import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepositoryImpl productRepositoryImpl;

    public Page<ProductResponseDto> searchProductItem(Pageable pageable,
                                                      String categoryFilter,
                                                      int minPriceFilter,
                                                      int maxPriceFilter,
                                                      int reviewFilter,
                                                      String keyWord) {
        validateKeyWord(keyWord);
        PageImpl<ProductResponseDto> searchItems = productRepositoryImpl.searchItems(pageable, categoryFilter, minPriceFilter, maxPriceFilter, reviewFilter, keyWord);
        validateResult(pageable, searchItems);
        return searchItems;
    }
    private void validateKeyWord(String keyWord) {
        if (keyWord == null || keyWord.isEmpty()) {
            throw new SearchException(ErrorCode.SEARCH_BLANK_KEYWORD);
        }
    }
    private void validateResult(Pageable pageable, Page<ProductResponseDto> searchItems) {
        if (searchItems.getTotalPages() == 0) {
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }

        if (searchItems.getTotalPages() < pageable.getPageSize()) {
            throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
        }
    }
}

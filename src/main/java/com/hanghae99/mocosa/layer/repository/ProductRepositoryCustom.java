package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<ProductResponseDto> searchItems(Pageable pageable, String categoryFilter, int minPriceFilter, int maxPriceFilter, int reviewFilter, String keyWord);
}

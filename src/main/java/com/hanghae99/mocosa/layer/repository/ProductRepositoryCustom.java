package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    List<SearchResponseDto> findAllByNameAndBrandName(Pageable pageable,
                                                      String keyword,
                                                      String categoryFilter,
                                                      Integer minPriceFilter, Integer maxPriceFilter,
                                                      Float reviewFilter);
}

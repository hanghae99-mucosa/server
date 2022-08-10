package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<SearchResponseDto> findBySearchRequestDto(SearchRequestDto searchRequestDto, Pageable pageable);
}

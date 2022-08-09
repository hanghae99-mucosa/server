package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ProductRepositoryCustom {
    Slice<SearchResponseDto> findBySearchRequestDto(SearchRequestDto searchRequestDto, Pageable pageable);
}

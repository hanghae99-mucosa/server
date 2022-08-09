package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.repository.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final int PAGEABLE_SIZE = 12;
    private final ProductRepositoryImpl productRepository;

    public Slice<SearchResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), PAGEABLE_SIZE);
        return productRepository.findBySearchRequestDto(searchRequestDto, pageable);
    }
}

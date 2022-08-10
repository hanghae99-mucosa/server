package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
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
        Slice<SearchResponseDto> searchResponseDtos = productRepository.findBySearchRequestDto(searchRequestDto, pageable);

//        // 요청된 페이지가 없을 경우
//        if(searchResponseDtos.)
        // 검색된 상품이 없을 겅우
        if(!searchResponseDtos.hasNext()){
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }
        return searchResponseDtos;
    }
}

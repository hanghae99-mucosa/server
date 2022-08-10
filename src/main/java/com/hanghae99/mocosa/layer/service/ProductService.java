package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<SearchResponseDto> searchProduct(int page, String sort, String categoryFilter, int minPriceFilter, int maxPriceFilter, float reviewFilter, String keyword) {

        // page size는 12으로 고정
        int size = 12;
        Pageable pageable = PageRequest.of(page, size);

        Page<SearchResponseDto> searchResponseDtos = productRepository.findAllByKeywordAndFilter(pageable, sort, categoryFilter, minPriceFilter, maxPriceFilter, reviewFilter, keyword);

        // 마지막 페이지 이상의 값이 들어갈 경우
        if(searchResponseDtos.getTotalPages() < page){
            throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
        }

        // 검색결과가 없을 경우
        if(searchResponseDtos.getContent().size() == 0){
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }

        return searchResponseDtos;
    }
}

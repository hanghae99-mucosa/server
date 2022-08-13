package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.SearchException;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.repository.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final int PAGEABLE_SIZE = 12;
    private final ProductRepositoryImpl productRepository;

    public Page<SearchResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        validateSort(searchRequestDto);

        validateBlankKeyword(searchRequestDto.getKeyword());

        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), PAGEABLE_SIZE);

        Page<SearchResponseDto> searchResponseDtos = productRepository.findBySearchRequestDto(searchRequestDto, pageable);

        int requestPage = searchResponseDtos.getTotalPages();
        int totalPage = searchRequestDto.getPage();

        validatePage(requestPage,totalPage);

        long totalElements = searchResponseDtos.getTotalElements();

        validateNoProduct(totalElements);

        return searchResponseDtos;
    }

    private void validateSort(SearchRequestDto searchRequestDto) {
        // 유효하지 않은 정렬조건이 넘어온 경우
        String sort = searchRequestDto.getSort();

        if(sort.equals("리뷰순") || sort.equals("저가순") || sort.equals("고가순")) {
            return;
        }

        throw new SearchException(ErrorCode.SEARCH_BAD_REQUEST);
    }

    private void validateBlankKeyword(String keyword) {
        if(keyword.isBlank()) {
            throw new SearchException(ErrorCode.SEARCH_BLANK_KEYWORD);
        }
    }

    private void validatePage(int totalPage, int requestPage) {
        // 마지막 페이지 이상의 값이 들어갈 경우
        if(totalPage < requestPage){
            throw new SearchException(ErrorCode.SEARCH_BAD_REQUEST);
        }
    }

    private void validateNoProduct(long totalElements) {
        // 검색결과가 없을 경우
        if(totalElements == 0){
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }
    }
}

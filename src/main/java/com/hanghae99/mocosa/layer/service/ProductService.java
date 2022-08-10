package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
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
/*
    private final ProductRepository productRepository;

    private Sort getSortType(String sort) {
        switch (sort) {
            case "리뷰순":
                return Sort.by("reviewNum").descending();
            case "저가순":
                return Sort.by("price").ascending();
            case "고가순":
                return Sort.by("price").descending();
            default: // 사용자가 임의로 존재하지 않는 파라미터로 변경하여 요청한 경우
                throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
        }
    }

    private void checkBlankKeyword(String keyword) {
        if(keyword.isBlank()) {
            throw new SearchException(ErrorCode.SEARCH_BLANK_KEYWORD);
        }
    }

    private void checkBadRequest(int page, Page<SearchResponseDto> searchResult) {
        if(searchResult.getTotalPages() < page) {
            throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
        }
    }

    private void checkNoResult(List<SearchResponseDto> searchResultList) {
        if(searchResultList.size() == 0) {
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }
    }

    public Page<SearchResponseDto> search(int page, String sort,
                                          String categoryFilter,
                                          Integer minPriceFilter, Integer maxPriceFilter,
                                          Float reviewFilter,
                                          String keyword) {

        checkBlankKeyword(keyword);

        Pageable pageable = PageRequest.of(page, 12, getSortType(sort));

        List<SearchResponseDto> searchResultList = productRepository.findAllByNameAndBrandName(pageable, keyword, categoryFilter, minPriceFilter, maxPriceFilter, reviewFilter);

        Page<SearchResponseDto> searchResult = new PageImpl<>(searchResultList, pageable, searchResultList.size());

        checkBadRequest(page, searchResult);

        checkNoResult(searchResultList);

        return searchResult;
*/
    private static final int PAGEABLE_SIZE = 12;
    private final ProductRepositoryImpl productRepository;

    public Page<SearchResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), PAGEABLE_SIZE);
        Page<SearchResponseDto> searchResponseDtos = productRepository.findBySearchRequestDto(searchRequestDto, pageable);

        // 마지막 페이지 이상의 값이 들어갈 경우
        if(searchResponseDtos.getTotalPages() < searchRequestDto.getPage()){
            throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
        }
        // 검색결과가 없을 경우
        if(searchResponseDtos.getContent().size() == 0){
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }
        return searchResponseDtos;
    }
}

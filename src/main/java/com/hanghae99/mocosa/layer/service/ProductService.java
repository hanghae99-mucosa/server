package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.MyPageException;
import com.hanghae99.mocosa.config.exception.custom.SearchException;
import com.hanghae99.mocosa.layer.dto.product.*;
import com.hanghae99.mocosa.layer.model.Brand;
import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepositoryImpl productRepositoryImpl;
    private final ProductRepository productRepository;

    private static final int PAGEABLE_SIZE = 12;

    public Page<SearchResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        validateSort(searchRequestDto);

        validateBlankKeyword(searchRequestDto.getKeyword());

        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), PAGEABLE_SIZE);

        Page<SearchResponseDto> searchResponseDtos = productRepositoryImpl.findBySearchRequestDto(searchRequestDto, pageable);

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

    public List<RestockListResponseDto> getRestockList(UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        List<RestockListResponseDto> restockList = productRepositoryImpl.findBySeller(user);

        validateNoRestockList(restockList);

        return restockList;
    }

//    public List<RestockListResponseDto> getRestockList() {
//
//        User user = new User(1L, "test1@test.com", "1234");
//
//        List<RestockListResponseDto> restockList = productRepositoryImpl.findBySeller(user);
//
//        validateNoRestockList(restockList);
//
//        return restockList;
//    }

    private void validateNoRestockList(List<RestockListResponseDto> restockList) {
        // 수량이 0개인 상품이 없는 경우
        if(restockList.size() == 0){
            throw new MyPageException(ErrorCode.MYPAGE_NO_DATA);
        }
    }

    public RestockResponseDto restock(RestockRequestDto restockRequestDto) {
        Long productId = restockRequestDto.getProductId();
        int amount = restockRequestDto.getAmount();

        validateAmount(amount);

        Product product = productRepository.findById(productId).get();

        product.update(amount);

        return new RestockResponseDto("재입고 등록이 완료되었습니다.");
    }

    private void validateAmount(int amount) {
        // 재입고 수량을 0으로 요청하는 경우
        if(amount == 0) {
            throw new MyPageException(ErrorCode.RESTOCK_BAD_REQUEST);
        }
    }
}

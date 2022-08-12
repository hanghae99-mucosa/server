package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.OrderException;
import com.hanghae99.mocosa.config.exception.custom.ProductException;
import com.hanghae99.mocosa.config.exception.custom.SearchException;
import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.Order;
import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.OrderRepository;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.ProductRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final int PAGEABLE_SIZE = 12;
    private final ProductRepositoryImpl productRepository;
    private final ProductRepository repository;
    private final OrderRepository orderRepository;

    public Page<SearchResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        validateSort(searchRequestDto);

        validateBlankKeyword(searchRequestDto.getKeyword());

        Pageable pageable = PageRequest.of(searchRequestDto.getPage(), PAGEABLE_SIZE);

        Page<SearchResponseDto> searchResponseDtos = productRepository.findBySearchRequestDto(searchRequestDto, pageable);

        int requestPage = searchResponseDtos.getTotalPages();
        int totalPage = searchRequestDto.getPage();

        validatePage(requestPage, totalPage);

        long totalElements = searchResponseDtos.getTotalElements();

        validateNoProduct(totalElements);

        return searchResponseDtos;
    }

    private void validateSort(SearchRequestDto searchRequestDto) {
        // 유효하지 않은 정렬조건이 넘어온 경우
        String sort = searchRequestDto.getSort();

        if (sort.equals("리뷰순") || sort.equals("저가순") || sort.equals("고가순")) {
            return;
        }

        throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
    }

    private void validateBlankKeyword(String keyword) {
        if (keyword.isBlank()) {
            throw new SearchException(ErrorCode.SEARCH_BLANK_KEYWORD);
        }
    }

    private void validatePage(int totalPage, int requestPage) {
        // 마지막 페이지 이상의 값이 들어갈 경우
        if (totalPage < requestPage) {
            throw new SearchException(ErrorCode.SEARCH_NO_PAGE);
        }
    }

    private void validateNoProduct(long totalElements) {
        // 검색결과가 없을 경우
        if (totalElements == 0) {
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }
    }

    @Transactional
    public ProductResponseDto getProductDetail(Long productId){

        // ErrorCode.DETAIL_ETC 를 잡기 위한 로직
        return getProductResponseDto(productId);
    }

    private ProductResponseDto getProductResponseDto(Long productId){
        Optional<Product> productByProductId = repository.findProductByProductId(productId);
        if (productByProductId.isEmpty()) {
            throw new ProductException(ErrorCode.DETAIL_NO_PRODUCT);
        }
        Product product = productByProductId.orElseThrow(() -> new ProductException(ErrorCode.DETAIL_ETC));

        ProductResponseDto productResponseDto;
        try {
            productResponseDto = new ProductResponseDto(product.getProductId(),
                    product.getName(),
                    product.getThumbnail(),
                    product.getBrandName(),
                    product.getCategoryName(),
                    product.getPrice(),
                    product.getAmount(),
                    product.getReviewNum(),
                    product.getReviewAvg());
        } catch (Exception e) {
            throw new ProductException(ErrorCode.DETAIL_ETC);
        }
        return productResponseDto;
    }

    @Transactional
    public String createOrder(Long productId, Integer orderAmount, User userDetails) {
        // ErrorCode.DETAIL_ETC 를 잡기 위한 로직

        return createOrderAndReduceProduct(productId, orderAmount, userDetails);
    }

    private String createOrderAndReduceProduct(Long productId, Integer orderAmount, User userDetails) {
        String result;
        Optional<Product> optional = repository.findById(productId);

        if (optional.isEmpty()) {
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }

        Product product = optional.orElseThrow(() -> new OrderException(ErrorCode.ORDER_ETC));


        // 가져온 상품의 수량과 사용자가 원하는 상품의 수량을 입력하게 한다.
        // throw new OrderException(ErrorCode.ORDER_NO_STOCK);
        if (orderAmount > product.getAmount()) {
            throw new OrderException(ErrorCode.ORDER_NO_STOCK);
        }

        //Product 에서 상품의 수량을 제거하고
        // TotalPrice 를 가져오게 한다.
        Integer totalPrice = product.orderProduct(orderAmount);


        // 상품의 수량이 문제 없이 깍였다면 Order 새로운 Order을 생성하고 저장한다.
        Order order = new Order(userDetails, product, orderAmount, totalPrice);
        orderRepository.save(order);

        result = "주문에 성공하셨습니다.";

        return result;
    }
}

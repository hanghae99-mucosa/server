package com.hanghae99.mocosa.layer.service;

import com .hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.OrderException;
import com.hanghae99.mocosa.config.exception.custom.ProductException;
import com.hanghae99.mocosa.config.exception.custom.SearchException;
import com.hanghae99.mocosa.layer.dto.order.OrderResponseDto;
import com.hanghae99.mocosa.layer.dto.product.ProductDetailResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.Order;
import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.RestockNotification;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.OrderRepository;
import com.hanghae99.mocosa.config.exception.custom.MyPageException;
import com.hanghae99.mocosa.layer.dto.product.*;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.RestockNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private static final int PAGEABLE_SIZE = 12;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final RestockNotificationRepository restockNotificationRepository;


    public Page<SearchResponseDto> getProducts(SearchRequestDto searchRequestDto) {
        validateSort(searchRequestDto);

        validateBlankKeyword(searchRequestDto.getKeyword());

        // 페이지를 1부터 시작
        Pageable pageable = PageRequest.of(searchRequestDto.getPage()-1, PAGEABLE_SIZE);

        Page<SearchResponseDto> searchResponseDtos = productRepository.findBySearchRequestDto(searchRequestDto, pageable);

        int totalPage = searchResponseDtos.getTotalPages();
        int requestPage = searchRequestDto.getPage();

        validatePage(totalPage, requestPage);

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

        throw new SearchException(ErrorCode.SEARCH_BAD_REQUEST);
    }

    private void validateBlankKeyword(String keyword) {
        if (keyword.isBlank()) {
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
        if (totalElements == 0) {
            throw new SearchException(ErrorCode.SEARCH_NO_PRODUCT);
        }
    }

    @Transactional
    public ProductDetailResponseDto getProductDetail(Long productId, UserDetailsImpl userDetails){

        // ErrorCode.DETAIL_ETC 를 잡기 위한 로직
        return getProductResponseDto(productId, userDetails);
    }

    private ProductDetailResponseDto getProductResponseDto(Long productId, UserDetailsImpl userDetails){


        Optional<Product> productByProductId = productRepository.findProductByProductId(productId);

        if (productByProductId.isEmpty()) {
            throw new ProductException(ErrorCode.DETAIL_NO_PRODUCT);
        }
        Product product = productByProductId.orElseThrow(() -> new ProductException(ErrorCode.DETAIL_ETC));

        Optional<RestockNotification> notification =
                restockNotificationRepository
                        .findRestockNotificationByUserAndProduct(userDetails.getUser(), product);

        //true 면 작동하게 한다.
        boolean empty = notification.isEmpty();

        ProductDetailResponseDto productResponseDto;
        try {
            productResponseDto = new ProductDetailResponseDto(
                    product.getProductId(),
                    product.getName(),
                    product.getThumbnail(),
                    product.getBrandName(),
                    product.getCategoryName(),
                    product.getPrice(),
                    product.getAmount(),
                    product.getReviewNum(),
                    product.getReviewAvg(),
                    empty);
            //
        } catch (Exception e) {
            throw new ProductException(ErrorCode.DETAIL_ETC);
        }
        return productResponseDto;
    }

    @Transactional
    public OrderResponseDto createOrder(Long productId, Integer orderAmount, User userDetails) {
        // ErrorCode.DETAIL_ETC 를 잡기 위한 로직

        return createOrderAndReduceProduct(productId, orderAmount, userDetails);
    }

    private OrderResponseDto createOrderAndReduceProduct(Long productId, Integer orderAmount, User userDetails) {
        Optional<Product> optional = productRepository.findById(productId);
        if (optional.isEmpty()) {
            throw new OrderException(ErrorCode.ORDER_ETC);
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
        Order order = Order.builder()
                .product(product)
                .user(userDetails)
                .totalPrice(totalPrice)
                .amount(orderAmount)
                .build();

        orderRepository.save(order);
        return new OrderResponseDto("주문에 성공하셨습니다.");
    }

    public List<RestockListResponseDto> getRestockList(UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        List<RestockListResponseDto> restockList = productRepository.findBySeller(user);

        validateNoRestockList(restockList);

        return restockList;
    }

    private void validateNoRestockList(List<RestockListResponseDto> restockList) {
        // 수량이 0개인 상품이 없는 경우
        if(restockList.size() == 0){
            throw new MyPageException(ErrorCode.MYPAGE_NO_DATA);
        }
    }

    @Transactional
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

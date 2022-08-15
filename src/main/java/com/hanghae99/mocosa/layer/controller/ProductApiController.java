package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.layer.dto.order.OrderRequestDto;
import com.hanghae99.mocosa.layer.dto.order.OrderResponseDto;
import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.dto.product.*;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @GetMapping("/api/search")
    public ResponseEntity<List<SearchResponseDto>> getProducts(SearchRequestDto searchRequestDto) {

        Page<SearchResponseDto> searchResponseDtoList = productService.getProducts(searchRequestDto);

        return new ResponseEntity(searchResponseDtoList, HttpStatus.OK);
    }


    // 상품 상세 페이지
    //상품 데이터 가져오기
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProductDetail(@PathVariable Long productId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductResponseDto result = productService.getProductDetail(productId);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping("/api/products/{productId}")
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long productId,
                                                        @RequestBody OrderRequestDto orderRequestDto
                                                        ,@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderResponseDto result = productService.createOrder(productId, orderRequestDto.getOrderAmount(), userDetails.getUser());
        return new ResponseEntity(result, HttpStatus.OK);

    @GetMapping("/api/users/restock")
    public ResponseEntity<List<RestockListResponseDto>> getRestockList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<RestockListResponseDto> restockList = productService.getRestockList(userDetails);

        return new ResponseEntity<>(restockList, HttpStatus.OK);
    }

//    @GetMapping("/api/users/restock")
//    public ResponseEntity<List<RestockListResponseDto>> getRestockList() {
//        List<RestockListResponseDto> restockList = productService.getRestockList();
//
//        return new ResponseEntity<>(restockList, HttpStatus.OK);
//    }

    @PutMapping("/api/users/restock")
    public ResponseEntity<RestockResponseDto> restock(@RequestBody RestockRequestDto restockRequestDto) {
        RestockResponseDto restockResponseDto = productService.restock(restockRequestDto);

        return new ResponseEntity<>(restockResponseDto, HttpStatus.OK);

    }
}

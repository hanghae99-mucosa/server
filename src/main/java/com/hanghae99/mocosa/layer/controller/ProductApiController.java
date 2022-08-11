package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;

    @GetMapping("/api/search")
    public ResponseEntity<List<SearchResponseDto>> getProducts(SearchRequestDto searchRequestDto){
    
        Page<SearchResponseDto> searchResponseDtoList = productService.getProducts(searchRequestDto);
        
        return new ResponseEntity(searchResponseDtoList, HttpStatus.OK);
    }

    // 상품 상세 페이지
    //상품 데이터 가져오기
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponseDto> getProductDetail(@PathVariable Long productId) {
        ProductResponseDto result = productService.getProductDetail(productId);
        return new ResponseEntity(result, HttpStatus.OK);
    }
}

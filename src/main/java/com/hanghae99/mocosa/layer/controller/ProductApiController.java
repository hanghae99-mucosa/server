package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductApiController {
    private final ProductService productService;

    @PostMapping("/api/search")
    public ResponseEntity<Page<ProductResponseDto>> searchProduct(
            Pageable pageable,
            @RequestParam(name = "categoryFilter") String categoryFilter,
            @RequestParam(name = "minPriceFilter", defaultValue = "0") int minPriceFilter,
            @RequestParam(name = "maxPriceFilter", defaultValue = "0") int maxPriceFilter,
            @RequestParam(name = "reviewFilter", defaultValue = "0") int reviewFilter,
            @RequestParam(name = "keyword") String keyWord
    ) {
        Page<ProductResponseDto> searchItems = productService.searchProductItem(
                pageable,
                categoryFilter,
                minPriceFilter,
                maxPriceFilter,
                reviewFilter,
                keyWord);

        return new ResponseEntity(searchItems, HttpStatus.OK);
    }
}

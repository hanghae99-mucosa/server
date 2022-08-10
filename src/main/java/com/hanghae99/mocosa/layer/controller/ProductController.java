package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/search")
    public ResponseEntity<Page<SearchResponseDto>> search(@RequestParam(defaultValue = "0", required = false) int page, @RequestParam(defaultValue = "리뷰순", required = false) String sort,
                                                    @RequestParam(required = false) String categoryFilter,
                                                    @RequestParam(defaultValue = "0", required = false) Integer minPriceFilter, @RequestParam(required = false) Integer maxPriceFilter,
                                                    @RequestParam(defaultValue = "0", required = false) Float reviewFilter,
                                                    @RequestParam String keyword) {
        Page<SearchResponseDto> searchResult = productService.search(page, sort, categoryFilter, minPriceFilter, maxPriceFilter, reviewFilter, keyword);

        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }
}

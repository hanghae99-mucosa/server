package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.config.exception.ErrorCode;
import com.hanghae99.mocosa.config.exception.SearchException;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/search")
    @ResponseBody
    public Page<SearchResponseDto> searchProduct(@RequestParam(required = false, defaultValue = "0") int page,
                                                 @RequestParam(required = false, defaultValue = "리뷰순") String sort,
                                                 @RequestParam(required = false, defaultValue = "전체") String categoryFilter,
                                                 @RequestParam(required = false, defaultValue = "0") int minPriceFilter,
                                                 @RequestParam(required = false, defaultValue = "2147483647") int maxPriceFilter,
                                                 @RequestParam(required = false, defaultValue = "0F") float reviewFilter,
                                                 @RequestParam String keyword) {
        if(keyword.isEmpty()){
            throw new SearchException(ErrorCode.SEARCH_BLANK_KEYWORD);
        }

        return productService.searchProduct(page, sort, categoryFilter, minPriceFilter, maxPriceFilter, reviewFilter, keyword);
    }
}

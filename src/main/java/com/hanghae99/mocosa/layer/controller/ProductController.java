package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/api/search")
    public ResponseEntity<List<SearchResponseDto>> getProducts(SearchRequestDto searchRequestDto){
    
        Page<SearchResponseDto> searchResponseDtoList = productService.getProducts(searchRequestDto);
        
        return new ResponseEntity(searchResponseDtoList, HttpStatus.OK);
    }
}

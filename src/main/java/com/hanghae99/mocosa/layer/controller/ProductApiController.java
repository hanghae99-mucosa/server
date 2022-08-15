package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.product.*;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

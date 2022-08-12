package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.order.OrderRequestDto;
import com.hanghae99.mocosa.layer.dto.product.ProductResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductApiController {

    private final ProductService productService;
    private final UserRepository userRepository;

    @GetMapping("/api/search")
    public ResponseEntity<List<SearchResponseDto>> getProducts(SearchRequestDto searchRequestDto) {

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

    @PostMapping("/api/products/{productId}")
    public String createOrder(@PathVariable Long productId,
                              @RequestBody OrderRequestDto orderRequestDto
//                             ,@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        // 아직 스프링 Security 가 완성이 안됬기 때문에 테스트 하기 위한 User 을 생성
        User userDetails = new User(4L,"test4@test.com", "1234");
        userRepository.save(userDetails);

        String order = productService.createOrder(productId, orderRequestDto.getOrderAmount(), userDetails);
        return order;
    }
}

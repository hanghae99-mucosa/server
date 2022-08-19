package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.layer.dto.order.OrderRequestDto;
import com.hanghae99.mocosa.layer.dto.order.OrderResponseDto;
import com.hanghae99.mocosa.layer.dto.product.ProductDetailResponseDto;
import com.hanghae99.mocosa.layer.dto.product.SearchRequestDto;
import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.dto.product.*;
import com.hanghae99.mocosa.layer.model.UserRoleEnum;
import com.hanghae99.mocosa.layer.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails, SearchRequestDto searchRequestDto) {
        if (userDetails != null) {
            model.addAttribute("email", userDetails.getUsername());

            if (userDetails.getRole() == UserRoleEnum.ADMIN) {
                model.addAttribute("admin", true);
            }
        }

        Page<SearchResponseDto> searchResponseDtoList = productService.getProducts(searchRequestDto);

        model.addAttribute("searchResponses", searchResponseDtoList);

        return "index";
    }

    @GetMapping("/search")
    public ResponseEntity<List<SearchResponseDto>> getProducts(SearchRequestDto searchRequestDto) {

        Page<SearchResponseDto> searchResponseDtoList = productService.getProducts(searchRequestDto);

        return new ResponseEntity(searchResponseDtoList, HttpStatus.OK);
    }


    // 상품 상세 페이지
    //상품 데이터 가져오기
    @GetMapping("/products/{productId}")
    public String getProductDetail(@PathVariable Long productId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetails,
                                   Model model) {
        ProductDetailResponseDto result = productService.getProductDetail(productId);
        model.addAttribute("product", result);
        return "product_detail";
    }

    @PostMapping("/products/{productId}")
    @ResponseBody
    public ResponseEntity<OrderResponseDto> createOrder(@PathVariable Long productId,
                                                        @RequestBody OrderRequestDto orderRequestDto
                                                        ,@AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        OrderResponseDto result = productService.createOrder(productId, orderRequestDto.getOrderAmount(), userDetails.getUser());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/users/restock")
    public String getRestockList(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<RestockListResponseDto> restockList = productService.getRestockList(userDetails);

        model.addAttribute("restockList", restockList);
        model.addAttribute("restockRequestDto", new RestockRequestDto());

        return "seller-mypage";
    }


    @PutMapping("/users/restock")
    public ResponseEntity<RestockResponseDto> restock(@RequestBody RestockRequestDto restockRequestDto, Model model) {
        RestockResponseDto restockResponseDto = productService.restock(restockRequestDto);

        model.addAttribute("restock", new RestockRequestDto());

        return new ResponseEntity<>(restockResponseDto, HttpStatus.OK);
    }
}

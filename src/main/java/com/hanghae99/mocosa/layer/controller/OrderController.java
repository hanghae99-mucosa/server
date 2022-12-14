package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.layer.dto.order.OrderCancelResponseDto;
import com.hanghae99.mocosa.layer.dto.order.OrderHistoryResponseDto;
import com.hanghae99.mocosa.layer.model.UserRoleEnum;
import com.hanghae99.mocosa.layer.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/users/orders")
    public String getOrderHistory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(defaultValue = "1") int page, Model model) {

        Page<OrderHistoryResponseDto> orderHistoryResponseDtoPage = orderService.getOrderHistory(userDetails, page);

        if (userDetails != null) {
            model.addAttribute("email", userDetails.getUsername());

            if (userDetails.getRole() == UserRoleEnum.ADMIN) {
                model.addAttribute("admin", true);
            }
        }

        model.addAttribute("orderHistorys", orderHistoryResponseDtoPage);

        return "user-mypage";
    }

    @DeleteMapping("/users/orders/{orderId}")
    public ResponseEntity<OrderCancelResponseDto> cancelOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        OrderCancelResponseDto orderCancelResponseDto = orderService.cancelOrder(orderId, userDetails);

        return new ResponseEntity<>(orderCancelResponseDto, HttpStatus.OK);
    }
}

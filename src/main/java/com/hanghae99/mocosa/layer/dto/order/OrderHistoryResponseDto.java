package com.hanghae99.mocosa.layer.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResponseDto {
    private Long orderId;
    private LocalDateTime createdAt;
    private String productName;
    private String thumbnail;
    private String brandName;
    private String category;
    private int totalPrice;
    private int orderAmount;

    public String getCreatedAt() {
        String createDate = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return createDate;
    }
}

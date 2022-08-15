package com.hanghae99.mocosa.layer.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestockListResponseDto {
    private Long productId;
    private String productName;
}

package com.hanghae99.mocosa.layer.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestockRequestDto {
    private Long productId;
    private int amount;
}

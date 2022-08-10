package com.hanghae99.mocosa.layer.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponseDto {
    private Long productId;
    private String name;
    private String thumbnail;
    private String brandName;
    private String category;
    private int price;
    private int amount;
    private int reviewNum;
    private float reviewAvg;
}

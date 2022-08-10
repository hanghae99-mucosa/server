package com.hanghae99.mocosa.layer.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponseDto {
    Long productId;
    String name;
    String thumnail;
    String brandName;
    String category;
    int price;
    int amount;
    int reviewNum;
    float reviewAvg;
}

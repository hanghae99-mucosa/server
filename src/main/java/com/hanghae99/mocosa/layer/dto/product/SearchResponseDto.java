package com.hanghae99.mocosa.layer.dto.product;

import com.hanghae99.mocosa.layer.model.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchResponseDto {
    Long productId;
    String name;
    String thumnail;
    String brandName;
    Category category;
    int price;
    int amount;
    int reviewNum;
    float reviewAvg;
}

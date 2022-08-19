package com.hanghae99.mocosa.layer.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDetailResponseDto {
    private final Long product_id;
    private final String name;
    private final String thumbnail;
    private final String brandName;
    private final String category;
    private final int price;
    private final int amount;
    private final int reviewNum;
    private final float reviewAvg;

    private final Boolean alert;

    public ProductDetailResponseDto(Long product_id,
                                    String name,
                                    String thumbnail,
                                    String brandName,
                                    String category,
                                    int price,
                                    int amount,
                                    int reviewNum,
                                    float reviewAvg,
                                    Boolean alert) {
        this.product_id = product_id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.brandName = brandName;
        this.category = category;
        this.price = price;
        this.amount = amount;
        this.reviewNum = reviewNum;
        this.reviewAvg = reviewAvg;
        this.alert = alert;
    }
}

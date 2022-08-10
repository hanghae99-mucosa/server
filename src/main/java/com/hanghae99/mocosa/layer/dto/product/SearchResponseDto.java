package com.hanghae99.mocosa.layer.dto.product;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    @QueryProjection
    public SearchResponseDto(Long productId, String name, String thumbnail, String brandName, String category, int price, int amount, int reviewNum, float reviewAvg) {
        this.productId = productId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.brandName = brandName;
        this.category = category;
        this.price = price;
        this.amount = amount;
        this.reviewNum = reviewNum;
        this.reviewAvg = reviewAvg;
    }
}

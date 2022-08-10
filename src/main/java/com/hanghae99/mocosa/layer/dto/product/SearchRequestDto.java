package com.hanghae99.mocosa.layer.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SearchRequestDto implements Serializable {
    int page;
    String sort;
    String categoryFilter;
    Integer minPriceFilter;
    Integer maxPriceFilter;
    Float reviewFilter;
    String keyword;

    public SearchRequestDto() {
        this.sort = "리뷰순";
    }
}

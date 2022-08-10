package com.hanghae99.mocosa.layer.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequestDto implements Serializable {
    int page;
    String sort;
    String categoryFilter;
    Integer minPriceFilter;
    Integer maxPriceFilter;
    Float reviewFilter;
    String keyword;

    public void defaultSort() {
        this.sort = "리뷰순";
    }
}

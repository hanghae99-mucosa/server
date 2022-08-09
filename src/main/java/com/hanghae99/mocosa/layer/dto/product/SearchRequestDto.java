package com.hanghae99.mocosa.layer.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {
    int page;
    String sort;
    String categoryFilter;
    Integer minPriceFilter;
    Integer maxPriceFilter;
    Float reviewFilter;
    String keyword;
}

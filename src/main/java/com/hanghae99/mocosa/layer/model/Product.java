package com.hanghae99.mocosa.layer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(length = 150, nullable = false)
    private String name;

    private String thumbnail;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private int price;

    private int amount;

    private int reviewNum;

    private float reviewAvg;
}

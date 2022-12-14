package com.hanghae99.mocosa.layer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(length = 150, nullable = false)
    private String name;

    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private int price;

    private int amount;

    private int reviewNum;

    private float reviewAvg;

    public String getBrandName() {
        return this.brand.getName();
    }

    public String getCategoryName() {
        return this.category.getCategory();
    }

    public Integer calculateTotalPrice(int amount) {
        return price * amount;
    }

    public void decrease(int orderAmount){
        this.amount -= orderAmount;
    }

    public void update(int amount) {
        this.amount = amount;
    }
}

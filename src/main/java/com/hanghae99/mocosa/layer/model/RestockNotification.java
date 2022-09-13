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
public class RestockNotification{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restockId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Boolean alarmFlag;

    public String getUserEmail() {
        return user.getEmail();
    }

    public String getProductName() {
        return product.getName();
    }

    public Long getProductId() {
        return product.getProductId();
    }

    public void checkAlarm() {
        this.alarmFlag = true;
    }
}
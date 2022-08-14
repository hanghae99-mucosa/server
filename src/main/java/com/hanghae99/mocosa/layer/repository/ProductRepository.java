package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}

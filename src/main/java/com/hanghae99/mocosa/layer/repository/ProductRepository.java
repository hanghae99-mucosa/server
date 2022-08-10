package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.dto.product.SearchResponseDto;
import com.hanghae99.mocosa.layer.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}

package com.hanghae99.mocosa.layer.repository;

import com.hanghae99.mocosa.layer.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    @Query("select p from Product p join fetch p.brand b join fetch p.category c where p.productId= :id")
    Optional<Product> findProductByProductId(@Param("id") Long id);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId=:id")
    Optional<Product> findByIdWithLock(Long id);
}

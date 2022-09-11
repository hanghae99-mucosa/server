package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final ProductRepository productRepository;
    private final RedissonClient redissonClient;

    public void reduceProductAmount(Long productId, Integer orderAmount) {

        RLock lock = redissonClient.getLock(productId.toString());

        try{
            boolean available = lock.tryLock(5, 3, TimeUnit.SECONDS);

            if(!available){
                return ;
            }

            reduceAmount(productId, orderAmount);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            if (lock != null && lock.isLocked()) {
                lock.unlock();
//            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reduceAmount(Long productId, Integer orderAmount) {
        Product product = productRepository.findById(productId).orElseThrow();

        product.decrease(orderAmount);

        productRepository.saveAndFlush(product);
    }
}

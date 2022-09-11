package com.hanghae99.mocosa.integration;

import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.service.ProductService;
import com.hanghae99.mocosa.layer.service.RedisService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcurrencyControlTest {
    public int lockCount = 0;
    public int unLockCount = 0;
    public static int CONCURRENCY_COUNT = 100;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    @AfterEach
    public void reset() {
        Product product = productRepository.findById(985L).orElseThrow();
        product.update(CONCURRENCY_COUNT);
        productRepository.save(product);
    }


    @Test
    public void 동시에_100명이_주문() throws InterruptedException {

        int threadCount = CONCURRENCY_COUNT;
        //멀티 쓰레드를 사용하기 위함, 비동기 작업을 단순화 해줌
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        //다른쓰레드의 작업이 끝날때까지 기다림
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {

                    reduceProductAmount(985L,1);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();


        Product product = productRepository.findById(985L).orElseThrow();

        System.out.println("남은 갯수 : "+product.getAmount());
        System.out.println("Lock 횟수 : "+lockCount);
        System.out.println("unLock 횟수 : "+unLockCount);

        assertEquals(0, product.getAmount());
        assertEquals(100, lockCount);
        assertEquals(100, unLockCount);

    }

    public void reduceProductAmount(Long productId, Integer orderAmount) {

        RLock lock = redissonClient.getLock(productId.toString());

        try{
            boolean available = lock.tryLock(5, 3, TimeUnit.SECONDS);

            if(!available){
                return ;
            } else {
                lockCount++;
            }

            reduceAmount(productId, orderAmount);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock != null && lock.isLocked()) {
                lock.unlock();
                unLockCount++;
            }
        }

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reduceAmount(Long productId, Integer orderAmount) {
        Product product = productRepository.findById(productId).orElseThrow();

        product.decrease(orderAmount);

        productRepository.saveAndFlush(product);
    }

}
package com.hanghae99.mocosa.integration;

import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcurrencyControlTest {
    public static int CONCURRENCY_COUNT = 5000;
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

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

                    productService.reduceProductAmount(985L,1);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Product product = productRepository.findById(985L).orElseThrow();

        assertEquals(0, product.getAmount());
    }
}
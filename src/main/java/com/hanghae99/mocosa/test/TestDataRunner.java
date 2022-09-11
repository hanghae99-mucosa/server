//package com.hanghae99.mocosa.test;
//
//import com.hanghae99.mocosa.layer.model.Product;
//import com.hanghae99.mocosa.layer.repository.ProductRepository;
//import com.hanghae99.mocosa.layer.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//@Component
//public class TestDataRunner implements ApplicationRunner {
//    public static int CONCURRENCY_COUNT = 100;
//
//    @Autowired
//    ProductService productService;
//
//    @Autowired
//    ProductRepository productRepository;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        double beforeTime = System.currentTimeMillis();
//
//        int threadCount = CONCURRENCY_COUNT;
//        //멀티 쓰레드를 사용하기 위함, 비동기 작업을 단순화 해줌
//        ExecutorService executorService = Executors.newFixedThreadPool(32);
//        //다른쓰레드의 작업이 끝날때까지 기다림
//        CountDownLatch latch = new CountDownLatch(threadCount);
//
//        for (int i = 0; i < threadCount; i++) {
//            executorService.submit(() -> {
//                try {
//
//                    productService.reduceProductAmount(985L,1);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//
//        Product product = productRepository.findById(985L).orElseThrow();
//
//        double afterTime = System.currentTimeMillis();
//        double secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
//
//        System.out.println("수량: "+product.getAmount());
//        System.out.println("시간차이(m) : "+secDiffTime);
//
//        product.update(CONCURRENCY_COUNT);
//        productRepository.save(product);
//        System.out.println("수량 reset 완료");
//    }
//}
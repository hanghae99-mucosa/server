package com.hanghae99.mocosa.test;

import com.hanghae99.mocosa.layer.model.*;
import com.hanghae99.mocosa.layer.repository.*;
import com.hanghae99.mocosa.config.jwt.PasswordEncoder;
import com.hanghae99.mocosa.layer.repository.BrandRepository;
import com.hanghae99.mocosa.layer.repository.CategoryRepository;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataRunner implements ApplicationRunner {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User testUser1 = new User("test1@test.com", passwordEncoder.encode("abc123123*"), UserRoleEnum.ADMIN);
        User testUser2 = new User("test2@test.com", passwordEncoder.encode("abc123123*"),UserRoleEnum.USER);
        User testUser3 = new User("test3@test.com", passwordEncoder.encode("abc123123*"),UserRoleEnum.ADMIN);
        User testUser4 = new User("test4@test.com", passwordEncoder.encode("abc123123*"),UserRoleEnum.USER);

        userRepository.save(testUser1);
        userRepository.save(testUser2);
        userRepository.save(testUser3);
        userRepository.save(testUser4);

        // Brand 생성
        Brand testBrand1 = new Brand(1L, "무신사 무탠다드", testUser1);
        Brand testBrand2 = new Brand(2L, "칼하트", testUser1);
        Brand testBrand3 = new Brand(3L, "마크곤잘레스", testUser2);
        Brand testBrand4 = new Brand(4L, "비바스튜디오", testUser2);
        Brand testBrand5 = new Brand(5L, "디스이즈네버댓", testUser3);
        Brand testBrand6 = new Brand(6L, "커버낫", testUser3);
        brandRepository.save(testBrand1);
        brandRepository.save(testBrand2);
        brandRepository.save(testBrand3);
        brandRepository.save(testBrand4);
        brandRepository.save(testBrand5);
        brandRepository.save(testBrand6);


        // Category 생성
        Category testCategory1 = new Category(1L, "상의", null);
        Category testCategory2 = new Category(2L, "바지", null);
        Category testCategory3 = new Category(3L, "반소매 티셔츠", 1L);
        Category testCategory4 = new Category(4L, "긴소매 티셔츠", 1L);
        Category testCategory5 = new Category(5L, "데님 팬츠", 2L);
        Category testCategory6 = new Category(6L, "코튼 팬츠", 2L);
        Category testCategory7 = new Category(7L, "슬랙스", 2L);
        categoryRepository.save(testCategory1);
        categoryRepository.save(testCategory2);
        categoryRepository.save(testCategory3);
        categoryRepository.save(testCategory4);
        categoryRepository.save(testCategory5);
        categoryRepository.save(testCategory6);
        categoryRepository.save(testCategory7);

        // Product 생성
        Product testProduct1 = new Product(1L, testBrand1, "릴렉스 핏 크루 넥 반팔 티셔츠", "image.png", testCategory1, 10690, 100, 69058, 4.8F);
        Product testProduct2 = new Product(2L, testBrand2, "K87 워크웨어 포켓 반팔티셔츠 (20colors)", "image2.png", testCategory3, 75030, 0, 590, 4.5F);
        Product testProduct3 = new Product(3L, testBrand3, "사인 로고 후드 그레이", "image3.png", testCategory4, 25000, 79, 234, 4.2F);
        Product testProduct4 = new Product(4L, testBrand4, "DRAWING DENIM PANTS", "image4.png", testCategory5, 37000, 50, 22233, 3.7F);
        Product testProduct5 = new Product(5L, testBrand5, "Cargo Pant Dusty Blue", "image5.png", testCategory6, 33400, 44, 990, 2.5F);
        Product testProduct6 = new Product(6L, testBrand6, "럭비 스트라이프 티셔츠 네이비", "image6.png", testCategory2, 13020, 9999, 40, 3.8F);
        Product testProduct7 = new Product(7L, testBrand1, "릴렉스 핏 크루 넥 긴팔 티셔츠", "image7.png", testCategory1, 20000, 100, 5000, 3F);
        Product testProduct8 = new Product(8L, testBrand1, "[쿨탠다드] 우먼즈 스트레이트 히든 밴딩 슬랙스 [블랙]", "image8.png", testCategory7, 34900, 0, 393, 4.7F);

        productRepository.save(testProduct1);
        productRepository.save(testProduct2);
        productRepository.save(testProduct3);
        productRepository.save(testProduct4);
        productRepository.save(testProduct5);
        productRepository.save(testProduct6);
        productRepository.save(testProduct7);
        productRepository.save(testProduct8);

        Order testOrder1 = new Order(1L, testUser4, testProduct1, 100, 1069000);

        orderRepository.save(testOrder1);
    }
}
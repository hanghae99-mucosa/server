package com.hanghae99.mocosa.example;

import com.hanghae99.mocosa.layer.model.Brand;
import com.hanghae99.mocosa.layer.model.Category;
import com.hanghae99.mocosa.layer.model.Product;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.BrandRepository;
import com.hanghae99.mocosa.layer.repository.CategoryRepository;
import com.hanghae99.mocosa.layer.repository.ProductRepository;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataRunner implements ApplicationRunner {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = User.builder()
                .email("jjucc99@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Brand nike = Brand.builder()
                .name("nike")
                .user(user)
                .build();

        brandRepository.save(nike);

        Category shoes = Category.builder()
                .category("shoes")
                .build();

        Category socks = Category.builder()
                .category("socks")
                .build();

        Category shirt = Category.builder()
                .category("shirt")
                .build();

        Category pants = Category.builder()
                .category("pants")
                .build();

        categoryRepository.save(shoes);
        categoryRepository.save(socks);
        categoryRepository.save(shirt);
        categoryRepository.save(pants);

        String thumbnail = "img.png";
        /*
          RED
          price 10000
          amount 100
          reviewNum 100
          reviewAvg 4
         */

        /*
          BLUE
          price 20000
          amount 200
          reviewNum 200
          reviewAvg 5
         */

        Product redShoes = Product.builder()
                .brand(nike)
                .name("redShoes")
                .thumbnail(thumbnail)
                .category(shoes)
                .price(10000)
                .amount(100)
                .reviewNum(100)
                .reviewAvg(4)
                .build();

        Product blueShoes = Product.builder()
                .brand(nike)
                .name("blueShoes")
                .thumbnail(thumbnail)
                .category(shoes)
                .price(20000)
                .amount(200)
                .reviewNum(200)
                .reviewAvg(5)
                .build();

        Product redShirt = Product.builder()
                .brand(nike)
                .name("redShirt")
                .thumbnail(thumbnail)
                .category(shirt)
                .price(100)
                .amount(100)
                .reviewNum(100)
                .reviewAvg(4)
                .build();


        Product blueShirt = Product.builder()
                .brand(nike)
                .name("blueShirt")
                .thumbnail(thumbnail)
                .category(shirt)
                .price(200000)
                .amount(200)
                .reviewNum(200)
                .reviewAvg(5)
                .build();

        Product redSocks = Product.builder()
                .brand(nike)
                .name("redSocks")
                .thumbnail(thumbnail)
                .category(socks)
                .price(10000)
                .amount(100)
                .reviewNum(10)
                .reviewAvg(4)
                .build();

        Product blueSocks = Product.builder()
                .brand(nike)
                .name("blueSocks")
                .thumbnail(thumbnail)
                .category(socks)
                .price(20000)
                .amount(200)
                .reviewNum(2000)
                .reviewAvg(5)
                .build();

        Product redPants = Product.builder()
                .brand(nike)
                .name("redPants")
                .thumbnail(thumbnail)
                .category(pants)
                .price(10000)
                .amount(100)
                .reviewNum(100)
                .reviewAvg(4)
                .build();

        Product bluePants = Product.builder()
                .brand(nike)
                .name("bluePants")
                .thumbnail(thumbnail)
                .category(pants)
                .price(20000)
                .amount(200)
                .reviewNum(200)
                .reviewAvg(5)
                .build();

        productRepository.save(redPants);
        productRepository.save(redSocks);
        productRepository.save(redShirt);
        productRepository.save(redShoes);
        productRepository.save(bluePants);
        productRepository.save(blueShoes);
        productRepository.save(blueSocks);
        productRepository.save(blueShirt);
    }
}

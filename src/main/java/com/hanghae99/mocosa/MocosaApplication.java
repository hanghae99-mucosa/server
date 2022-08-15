package com.hanghae99.mocosa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaAuditing
public class MocosaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MocosaApplication.class, args);
    }
}

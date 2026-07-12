package com.example.ecommercesystemproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class EcommercesystemprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommercesystemprojectApplication.class, args);
    }

}

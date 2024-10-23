package org.example.cookbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CookBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookBookApplication.class, args);
    }

}

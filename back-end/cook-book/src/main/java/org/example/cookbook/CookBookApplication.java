package org.example.cookbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
public class CookBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookBookApplication.class, args);
    }

}

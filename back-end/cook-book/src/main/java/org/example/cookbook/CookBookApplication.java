package org.example.cookbook;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableCaching
@EnableMethodSecurity
@OpenAPIDefinition(info = @Info(title = "Cook Book API", version = "1.0", description = "Cook Book Microservice"))
public class CookBookApplication {

    public static void main(String[] args) {
        SpringApplication.run(CookBookApplication.class, args);
    }

}

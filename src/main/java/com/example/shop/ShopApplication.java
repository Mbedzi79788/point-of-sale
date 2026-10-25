package com.example.shop;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class ShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    /** Seeds a few demo products on startup. */
    @Bean
    CommandLineRunner seed(ProductRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                repo.save(new Product("Coffee Mug", "Ceramic mug, 350ml", new BigDecimal("8.50")));
                repo.save(new Product("Notebook", "A5 dotted notebook, 120 pages", new BigDecimal("5.00")));
                repo.save(new Product("Backpack", "Water-resistant daypack, 20L", new BigDecimal("39.99")));
                repo.save(new Product("Desk Lamp", "LED lamp with adjustable arm", new BigDecimal("24.90")));
                repo.save(new Product("Water Bottle", "Insulated steel bottle, 750ml", new BigDecimal("16.75")));
                repo.save(new Product("Headphones", "Over-ear wired headphones", new BigDecimal("29.00")));
            }
        };
    }
}

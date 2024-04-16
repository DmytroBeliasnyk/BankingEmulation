package org.emuba.bankingemulation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class BankingEmulationApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingEmulationApplication.class, args);
    }
}

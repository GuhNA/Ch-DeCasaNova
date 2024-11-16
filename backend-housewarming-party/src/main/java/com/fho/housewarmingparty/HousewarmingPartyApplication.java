package com.fho.housewarmingparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class HousewarmingPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HousewarmingPartyApplication.class, args);
    }
}

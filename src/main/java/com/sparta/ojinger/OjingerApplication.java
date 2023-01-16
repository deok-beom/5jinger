package com.sparta.ojinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OjingerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjingerApplication.class, args);
    }

}

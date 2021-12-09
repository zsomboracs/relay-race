package com.example.relayrace;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppConfiguration {

    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public ExecutorService executor() {
        return Executors.newFixedThreadPool(100);
    }

}

package com.cardcost.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConnectionChecker {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Bean
    public ApplicationRunner checkRedisConnection() {
        return args -> {
            boolean connected = false;
            int attempts = 0;

            while (!connected && attempts < 5) {
                try {
                    attempts++;

                    redisTemplate.opsForValue().set("testKey", "testValue");

                    String value = redisTemplate.opsForValue().get("testKey");

                    if ("testValue".equals(value)) {
                        System.out.println("Successfully connected to Redis. Test value retrieved: " + value);
                        connected = true;
                    } else {
                        System.out.println("Connected to Redis, but the test value does not match.");
                    }
                } catch (Exception e) {
                    System.err.println("Error connecting to Redis on attempt " + attempts + ": " + e.getMessage());
                    Thread.sleep(2000);
                }
            }

            if (!connected) {
                System.err.println("Unable to connect to Redis after " + attempts + " attempts.");
            }
        };
    }
}

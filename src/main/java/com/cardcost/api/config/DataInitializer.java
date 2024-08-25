package com.cardcost.api.config;

import com.cardcost.api.model.ClearingCost;
import com.cardcost.api.repository.ClearingCostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Autowired
    private ClearingCostRepository repository;

    @Bean
    public ApplicationRunner initializer() {

        return args -> {
            if (repository.count() == 0) {
                repository.save(new ClearingCost(null, "US", 5.0));
                repository.save(new ClearingCost(null, "GR", 15.0));
                repository.save(new ClearingCost(null, "Others", 10.0));
            }
        };
    }
}


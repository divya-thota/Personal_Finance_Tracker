package com.personal.finance_tracker_service.splitwise;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SplitwiseConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

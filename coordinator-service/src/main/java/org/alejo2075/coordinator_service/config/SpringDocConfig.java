package org.alejo2075.coordinator_service.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springdoc")
                .packagesToScan("org.alejo2075.coordinator_service.controller")
                .build();
    }
}
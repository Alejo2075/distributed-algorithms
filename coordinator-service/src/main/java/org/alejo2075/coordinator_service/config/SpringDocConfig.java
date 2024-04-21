package org.alejo2075.coordinator_service.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Value("${api.version}")
    private String apiVersion;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springdoc-api-v" + apiVersion)
                .packagesToScan("org.alejo2075.coordinator_service.controller")
                .addOpenApiCustomiser(openApi -> {
                    openApi.info(new io.swagger.v3.oas.models.info.Info()
                            .title("Coordinator Service API")
                            .version(apiVersion)
                            .description("This API handles the coordination of various sorting and searching algorithms in a distributed microservices environment.")
                            .termsOfService("http://termsOfServiceUrl.com")
                            .contact(new io.swagger.v3.oas.models.info.Contact()
                                    .name("Alejandro Santana")
                                    .url("https://github.com/Alejo2075")
                                    .email("alejsant75@gmail.com"))
                            .license(new io.swagger.v3.oas.models.info.License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html")));
                })
                .build();
    }
}

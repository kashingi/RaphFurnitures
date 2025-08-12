package com.raph_furniture.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI raphFurnitureOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RAPH FURNITURE API")
                        .description("""
                                Backend API for Raph Furniture (Spring Boot + JWT).

                                Developed by **Lemaiyan** and **Juma**
                                """)

                        .version("v1.0")
                        .contact(new Contact()
                                .name("Raph Furniture Dev Team")
                                .email("support@raph-furniture.com"))
                        .license(new License().name("Apache 2.0")))
                // Add the global security requirement so the lock icon shows on endpoints
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(
                        SECURITY_SCHEME_NAME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME_NAME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                ));
    }

    // Optional but nice: group & limit scanning to your base package for faster startup.
    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("rapf-api")
                .packagesToScan("com.raph_furniture") // your root package
                // .pathsToMatch("/api/**") // uncomment if you want to include only /api/**
                .build();
    }
}

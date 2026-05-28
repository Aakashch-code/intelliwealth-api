package com.example.intelliwealth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("IntelliWealth API")
                        .version("2.0.0")
                        .description("""
                                IntelliWealth is a modular financial management platform
                                designed to help users manage and analyze their complete
                                financial ecosystem.

                                Features:
                                - Asset Management
                                - Budget Tracking
                                - Debt & EMI Management
                                - Goal Planning
                                - Insurance Management
                                - Subscription Tracking
                                - Transaction Analytics
                                - Net Worth Analysis
                                - AI Financial Assistant (Fynix)
                                - PDF Export & Reports
                                """)
                        .contact(new Contact()
                                .name("Aakash")
                                .email("aakashch.code@gmail.com")
                        )
                )

                .addSecurityItem(
                        new SecurityRequirement().addList(SECURITY_SCHEME_NAME)
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        SECURITY_SCHEME_NAME,
                                        new SecurityScheme()
                                                .name(SECURITY_SCHEME_NAME)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .description("Enter JWT token in format: Bearer <token>")
                                )
                );
    }
}
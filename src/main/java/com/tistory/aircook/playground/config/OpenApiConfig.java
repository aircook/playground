package com.tistory.aircook.playground.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "Bearer Authentication";
        
        return new OpenAPI()
                .info(new Info()
                        .title("Playground API Documentation")
                        .description("Spring Boot 플레이그라운드 프로젝트 API 문서")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("francis.lee")
                                .url("https://tistory.aircook")
                                .email("aircook@tistory.com")))
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT Token")));
    }
}

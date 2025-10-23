package com.example.jwtapi.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JWT API - Gestión de Productos")
                        .description("API REST completa para autenticación JWT y gestión de productos con Spring Boot. " +
                                "Incluye endpoints para autenticación, registro de usuarios, CRUD de productos y endpoints protegidos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Desarrollador")
                                .email("developer@example.com")
                                .url("https://github.com/example/jwt-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor de desarrollo"),
                        new Server().url("https://api.example.com").description("Servidor de producción")
                ))
                .tags(List.of(
                        new Tag().name("Autenticación").description("Endpoints para autenticación y registro de usuarios"),
                        new Tag().name("Productos").description("Endpoints para gestión completa de productos"),
                        new Tag().name("Endpoints Protegidos").description("Endpoints que requieren autenticación JWT")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}

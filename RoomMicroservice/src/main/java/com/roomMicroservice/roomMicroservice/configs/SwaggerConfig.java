package com.roomMicroservice.roomMicroservice.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Room API")
                        .version("1.0")
                        .description("Service documentation for the room API")
                        .contact(new Contact()
                                .name("Mikhailov Kirill")
                                .email("akcjdjs123456789@gmail.com")))
                .servers(List.of(new Server().url("http://localhost:8002/api").description("Room service"), new Server().url("http://localhost:8001/api").description("User service")));

    }

    @Bean
    public GroupedOpenApi adminApi(){
        return GroupedOpenApi.builder()
                .group("rooms")
                .pathsToMatch("/api/v1/rooms/**")
                .build();
    }

}

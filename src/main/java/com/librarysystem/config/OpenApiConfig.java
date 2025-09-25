package com.librarysystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI librarySystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Library System API")
                        .description("RESTful API for managing books, authors, borrowers, and loans in a library system")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Library System Team")
                                .email("support@librarysystem.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}

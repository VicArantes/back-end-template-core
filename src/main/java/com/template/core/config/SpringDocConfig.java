package com.template.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Esta classe configura Swagger UI.
 */
@Configuration
public class SpringDocConfig {

    /**
     * Este Bean configura a inserção do bearer token JWT.
     */
    @Bean
    public OpenAPI custinOpenApi() {
        return new OpenAPI().components(new Components().addSecuritySchemes("bearer-key", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}
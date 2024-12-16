package com.template.core;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Class responsável pela inicialização do projeto.
 */
@OpenAPIDefinition(servers = {@Server(url = "/template-core/", description = "Swagger TemplateCoreApplication")})
@SpringBootApplication
public class TemplateCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateCoreApplication.class, args);
    }

}

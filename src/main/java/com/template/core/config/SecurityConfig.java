package com.template.core.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;

/**
 * Esta classe configura a segurança do Spring Security na aplicação.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${api.key}")
    public String apiKey;

    /**
     * Valida a chave da API comparando a versão decodificada da chave recebida com a chave esperada.
     *
     * @param key A chave da API codificada em Base64 a ser validada.
     * @return {@code true} se a chave decodificada corresponder à chave esperada, {@code false} caso contrário.
     * @throws AccessDeniedException Se houver um erro ao decodificar a chave.
     */
    private boolean validateAPIKey(String key) {
        try {
            String decodedKey = new String(Base64.getDecoder().decode(key.getBytes()));
            return decodedKey.equals(apiKey);
        } catch (Exception e) {
            throw new AccessDeniedException(e.getMessage());
        }
    }

    /**
     * Configura o filtro de segurança para as requisições HTTP.
     *
     * @param http O objeto HttpSecurity usado para configurar a segurança.
     * @return O SecurityFilterChain configurado.
     * @throws Exception Se ocorrer algum erro durante a configuração.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/**")
                        .permitAll()
                        .anyRequest()
                        .access(((_, request) -> {
                            String tempApiKey = request.getRequest().getHeader("X-Service-Token");

                            if (validateAPIKey(tempApiKey)) {
                                return new AuthorizationDecision(true);
                            }
                            throw new AccessDeniedException("ACESSO NEGADO");
                        }))


                );
        return http.build();
    }

}
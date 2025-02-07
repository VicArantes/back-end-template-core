package com.template.core.config;

import com.template.core.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Base64;

/**
 * Esta classe configura a segurança do Spring Security na aplicação.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenService tokenService;
    private final UserService userService;

    @Value("${api.key}")
    public String apiKey;

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
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui.html/**", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        .anyRequest().access((authenticationSupplier, request) -> {
                            String tempApiKey = request.getRequest().getHeader("X-Service-Token");

                            if (validateAPIKey(tempApiKey)) {
                                return new AuthorizationDecision(true);
                            }
                            throw new AccessDeniedException("Access Denied");
                        }))
                .addFilterBefore(new AuthenticationFilter(tokenService, userService), UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Configura o codificador de senhas utilizado para criptografar senhas.
     *
     * @return O PasswordEncoder configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura o gerenciador de autenticação utilizado para autenticar usuários.
     *
     * @param http               O objeto HttpSecurity usado para configurar a segurança.
     * @param passwordEncoder    O codificador de senhas utilizado para criptografar senhas.
     * @param userDetailsService O serviço de detalhes do usuário utilizado para recuperar detalhes do usuário.
     * @return O AuthenticationManager configurado.
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, AuthenticationService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

}
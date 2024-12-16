package com.template.core.config;

import com.template.core.service.AuthorityService;
import com.template.core.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Arquivo responsável por adicionar dados iniciais cruciais para o funcionamento do projeto.
 */
@RequiredArgsConstructor
@Service
public class Initializer {
    private final AuthorityService authorityService;
    private final UserService userService;

    /**
     * Função responsável por adicionar dados iniciais cruciais para o funcionamento do projeto.
     */
    @PostConstruct
    public void postConstruct() {
        authorityService.addAuthorityAdmin();
        userService.addAdmin();
    }
}

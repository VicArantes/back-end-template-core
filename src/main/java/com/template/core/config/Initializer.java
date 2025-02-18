package com.template.core.config;

import com.template.core.service.PermissaoService;
import com.template.core.service.RoleService;
import com.template.core.service.DadosPessoaisService;
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
    private final RoleService roleService;
    private final UserService userService;
    private final DadosPessoaisService dadosPessoaisService;
    private final PermissaoService permissaoService;

    /**
     * Função responsável por adicionar dados iniciais cruciais para o funcionamento do projeto.
     */
    @PostConstruct
    public void postConstruct() {
        roleService.addRoleAdmin();
        dadosPessoaisService.addDadosPessoaisAdmin();
        userService.addAdmin();
        permissaoService.adicionaPermissoes();
    }
}

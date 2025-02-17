package com.template.core.service;

import com.template.core.entity.GrupoAcesso;
import com.template.core.entity.Permissao;
import com.template.core.entity.Role;
import com.template.core.entity.Rota;
import com.template.core.enums.Acesso;
import com.template.core.repository.GrupoAcessoRepository;
import com.template.core.repository.PermissaoRepository;
import com.template.core.repository.RoleRepository;
import com.template.core.repository.RotaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

/**
 * Serviço para manipulação de roles.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RoleService {
    private final RoleRepository repository;
    private final PermissaoRepository permissaoRepository;
    private final RotaRepository rotaRepository;
    private final GrupoAcessoRepository grupoAcessoRepository;

    /**
     * Busca uma role pelo ID.
     *
     * @param id o ID do role a ser buscada
     * @return a role encontrada
     * @throws EntityNotFoundException se a role não for encontrada
     */
    public Role findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role não encontrada."));
    }

    /**
     * Retorna uma página de roles.
     *
     * @param pageable informações de paginação
     * @return a página de roles
     */
    public Page<Role> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Salva uma role.
     *
     * @param role a role a ser salva
     * @return a role salva
     * @throws IllegalStateException se a role já possui um ID atribuído
     */
    public Role save(Role role) {
        if (role.getId() == null) {
            return repository.save(role);
        }

        throw new IllegalStateException("Entidade já possui um ID, utilizar a requisição de update.");
    }

    /**
     * Atualiza uma role.
     *
     * @param role a role a ser atualizada
     * @return a role atualizada
     * @throws EntityNotFoundException se a role não for encontrada
     */
    public Role update(Role role) {
        if (repository.findById(role.getId()).isPresent()) {
            return repository.save(role);
        }

        throw new EntityNotFoundException(MessageFormat.format("Role com ID {0} não encontrada.", role.getId()));
    }

    /**
     * Exclui uma role pelo ID.
     *
     * @param id o ID da role a ser excluída
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Adiciona role ADMIN no sistema.
     */
    public void addRoleAdmin() {
        if (repository.count() == 0) {
            Set<Permissao> permissoes = Set.of(
                    permissaoRepository.save(new Permissao(null, "Listar usuários", "/api/user/find")),
                    permissaoRepository.save(new Permissao(null, "Buscas usuário por ID", "/api/user/get/{id}")),
                    permissaoRepository.save(new Permissao(null, "Salvar usuário", "/api/user/save")),
                    permissaoRepository.save(new Permissao(null, "Atualizar usuário", "/api/user/update"))
            );

            Rota rota = rotaRepository.save(new Rota(null, "Gerencia usuários", "/usuarios", permissoes));
            GrupoAcesso grupoAcesso = grupoAcessoRepository.save(new GrupoAcesso(null, rota, List.of(Acesso.DELETE, Acesso.WRITE, Acesso.UPDATE, Acesso.READ)));

            repository.save(new Role(null, "ADMIN", Set.of(grupoAcesso)));
        }
    }

}

package com.template.core.service;

import com.template.core.entity.Authority;
import com.template.core.repository.AuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço para manipulação de authorities.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class AuthorityService {
    private final AuthorityRepository repository;

    /**
     * Busca uma authority pelo ID.
     *
     * @param id o ID do authority a ser buscada
     * @return a authority encontrada
     * @throws EntityNotFoundException se a authority não for encontrada
     */
    public Authority findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Authority não encontrada."));
    }

    /**
     * Retorna uma página de authorities.
     *
     * @param pageable informações de paginação
     * @return a página de authorities
     */
    public Page<Authority> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Salva uma authority.
     *
     * @param authority a authority a ser salva
     * @return a authority salva
     * @throws IllegalStateException se a authority já possui um ID atribuído
     */
    public Authority save(Authority authority) {
        if (authority.getId() == null) {
            return repository.save(authority);
        }

        throw new IllegalStateException("Entidade já possui um ID, utilizar a requisição de update.");
    }

    /**
     * Atualiza uma authority.
     *
     * @param authority a authority a ser atualizada
     * @return a authority atualizada
     * @throws EntityNotFoundException se a authority não for encontrada
     */
    public Authority update(Authority authority) {
        if (repository.findById(authority.getId()).isPresent()) {
            return repository.save(authority);
        }

        throw new EntityNotFoundException("Authority com ID " + authority.getId() + " não encontrada.");
    }

    /**
     * Exclui uma authority pelo ID.
     *
     * @param id o ID da authority a ser excluída
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Adiciona authority ADMIN no sistema.
     */
    public void addAuthorityAdmin() {
        if (repository.count() == 0) {
            repository.save(new Authority(null, "ADMIN"));
        }
    }

}

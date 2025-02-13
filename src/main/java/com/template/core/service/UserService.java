package com.template.core.service;

import com.template.core.entity.User;
import com.template.core.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço para manipulação de users.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {
    private final UserRepository repository;
    private final AuthorityRepository authorityRepository;

    /**
     * Senha padrão do admin.
     */
    @Value("${template.admin.password}")
    private String adminPassword;

    private BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Busca um user pelo ID.
     *
     * @param id o ID do user a ser buscado
     * @return o user encontrado
     * @throws EntityNotFoundException se o user não for encontrado
     */
    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("User não encontrado"));
    }

    /**
     * Retorna uma página de users.
     *
     * @param pageable informações de paginação
     * @return a página de users
     */
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Salva um user.
     *
     * @param user o user a ser salvo
     * @return o user salvo
     * @throws IllegalStateException se o user já possui um ID atribuído
     */
    public User save(User user) {
        if (user.getId() == null) {
            user.setPassword((bCryptPasswordEncoder().encode(user.getPassword())));
            return repository.save(user);
        }

        throw new IllegalStateException("Entidade já possui um ID, utilizar a requisição de update.");
    }

    /**
     * Atualiza um user.
     *
     * @param user o user a ser atualizado
     * @return a authority atualizada
     * @throws EntityNotFoundException se o user não for encontrado
     */
    public User update(User user) {
        if (repository.findById(user.getId()).isPresent()) {
            return repository.save(user);
        }

        throw new EntityNotFoundException("User com ID " + user.getId() + " não encontrado.");
    }

    /**
     * Exclui um user pelo ID.
     *
     * @param id o ID do user a ser excluído
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Adiciona o ADMIN do sistema.
     */
    public void addAdmin() {
        if (repository.count() == 0) {
            repository.save(new User(null, "admin", bCryptPasswordEncoder().encode(adminPassword), "admin@admin.com", Status.ATIVO, new ArrayList<>(List.of(authorityRepository.findByAuthorityName("ADMIN")))));
        }
    }

}
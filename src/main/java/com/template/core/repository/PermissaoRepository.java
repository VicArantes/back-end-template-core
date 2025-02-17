package com.template.core.repository;

import com.template.core.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository para entidade Permissao.
 */
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    Optional<Permissao> findByEndpoint(String endpoint);

}
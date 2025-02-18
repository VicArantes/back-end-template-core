package com.template.core.repository;

import com.template.core.entity.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository para entidade Permissao.
 */
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    Optional<Permissao> findByEndpoint(String endpoint);

    @Modifying
    @Transactional
    @Query(value = "UPDATE              " +
            "           Permissao p     " +
            "       SET                 " +
            "           p.ativo = false " +
            "       WHERE               " +
            "           p.id = :id      ")
    void setInativo(@Param("id") Long id);

}
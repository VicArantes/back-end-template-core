package com.template.core.repository;

import com.template.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository para entidade Role.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNome(String nome);

    @Modifying
    @Transactional
    @Query(value = "UPDATE              " +
            "           Role r          " +
            "       SET                 " +
            "           r.ativo = false " +
            "       WHERE               " +
            "           r.id = :id      ")
    void setInativo(@Param("id") Long id);

}
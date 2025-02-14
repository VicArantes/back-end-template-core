package com.template.core.repository;

import com.template.core.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository para entidade Role.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNome(String nome);

}
package com.template.core.repository;

import com.template.core.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository para entidade Authority.
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthorityName(String authorityName);
}
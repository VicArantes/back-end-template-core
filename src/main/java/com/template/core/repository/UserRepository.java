package com.template.core.repository;

import com.template.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository para entidade User.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
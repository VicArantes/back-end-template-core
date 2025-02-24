package com.template.core.repository;

import com.template.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository para entidade User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE              " +
            "           User u          " +
            "       SET                 " +
            "           u.ativo = false " +
            "       WHERE               " +
            "           u.id = :id      ")
    void setInativo(@Param("id") Long id);

}
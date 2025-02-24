package com.template.core.repository;

import com.template.core.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository para entidade Produto.
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE              " +
            "           Produto p       " +
            "       SET                 " +
            "           p.ativo = false " +
            "       WHERE               " +
            "           p.id = :id      ")
    void setInativo(@Param("id") Long id);

}
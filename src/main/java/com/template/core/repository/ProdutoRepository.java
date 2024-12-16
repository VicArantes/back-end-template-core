package com.template.core.repository;

import com.template.core.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository para entidade Produto.
 */
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
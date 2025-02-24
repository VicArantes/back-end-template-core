package com.template.core.repository;

import com.template.core.entity.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para entidade Rota.
 */
@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {
}
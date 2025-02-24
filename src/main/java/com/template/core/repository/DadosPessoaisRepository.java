package com.template.core.repository;

import com.template.core.entity.DadosPessoais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repository para entidade DadosPessoais.
 */
@Repository
public interface DadosPessoaisRepository extends JpaRepository<DadosPessoais, Long> {

    Optional<DadosPessoais> findByCpfCnpj(String cpfCnpj);

    @Modifying
    @Transactional
    @Query(value = "UPDATE                  " +
            "           DadosPessoais dp    " +
            "       SET                     " +
            "           dp.ativo = false    " +
            "       WHERE                   " +
            "           dp.id = :id         ")
    void setInativo(@Param("id") Long id);

}
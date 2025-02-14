package com.template.core.repository;

import com.template.core.entity.DadosPessoais;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository para entidade DadosPessoais.
 */
public interface DadosPessoaisRepository extends JpaRepository<DadosPessoais, Long> {

    Optional<DadosPessoais> findByCpfCnpj(String cpfCnpj);


}
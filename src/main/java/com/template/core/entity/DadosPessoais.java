package com.template.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Entidade que representa os dados pessoais de um usuário.
 */
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "dados_pessoais")
public class DadosPessoais {

    /**
     * Identificador único dos dados pessoais de um usuário.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * Nome do usuário.
     */
    @Column(name = "tx_nome")
    @ColumnTransformer(write = "UPPER(?)")
    @NotBlank
    private String nome;

    /**
     * CPF/CNPJ do usuário.
     */
    @Column(name = "tx_cpf_cnpj", unique = true)
    @NotBlank
    private String cpfCnpj;

}
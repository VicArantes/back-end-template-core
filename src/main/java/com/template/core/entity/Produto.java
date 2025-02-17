package com.template.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

/**
 * Entidade que representa um produto.
 */
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "produtos")
public class Produto {

    /**
     * Identificador único do produto.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * Descrição do produto.
     */
    @Column(name = "tx_descricao", unique = true)
    @ColumnTransformer(write = "UPPER(?)")
    @NotBlank
    private String descricao;

    /**
     * Identificador para verificar se o produto está ativo.
     */
    @Column(name = "bl_ativo")
    @NotNull
    private boolean ativo;

}
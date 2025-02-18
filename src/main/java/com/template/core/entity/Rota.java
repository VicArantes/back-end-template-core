package com.template.core.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Entidade que representa uma rota.
 */
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "rotas")
public class Rota {

    /**
     * Identificador único da rota.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * Descrição da rota.
     */
    @Column(name = "tx_descricao")
    @NotBlank
    private String descricao;

    /**
     * URL da rota.
     */
    @Column(name = "tx_url", unique = true)
    @NotBlank
    private String url;

    /**
     * Lista de permissões da rota.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @NotEmpty
    private Set<Permissao> permissoes;

    /**
     * Identificador para verificar se a rota está ativa.
     */
    @Column(name = "bl_ativo")
    @NotNull
    private boolean ativo;

}
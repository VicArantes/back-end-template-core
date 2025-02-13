package com.template.core.entity;

import com.template.core.enums.Acesso;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entidade que representa um grupo de acesso.
 */
@AllArgsConstructor
@Data
@Entity
@NoArgsConstructor
@Table(name = "grupos_acesso")
public class GrupoAcesso {

    /**
     * Identificador único do grupo de acesso.
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    /**
     * Rota do grupo de acesso.
     */
    @OneToOne
    @NotNull
    private Rota rota;

    /**
     * Lista de acessos do grupo de acesso.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @NotEmpty
    private List<Acesso> acessos;

}
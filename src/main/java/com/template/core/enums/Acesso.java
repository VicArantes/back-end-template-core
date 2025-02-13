package com.template.core.enums;

/**
 * Representa os diferentes níveis de acesso disponíveis no sistema.
 */
public enum Acesso {

    /**
     * Permissão para leitura de dados.
     * Usuários com este acesso podem visualizar informações, mas não modificá-las.
     */
    READ,

    /**
     * Permissão para escrita de dados.
     * Usuários com este acesso podem criar ou adicionar novas informações ao sistema.
     */
    WRITE,

    /**
     * Permissão para atualização de dados.
     * Usuários com este acesso podem modificar informações existentes.
     */
    UPDATE,

    /**
     * Permissão para exclusão de dados.
     * Usuários com este acesso podem remover informações do sistema.
     */
    DELETE;
}
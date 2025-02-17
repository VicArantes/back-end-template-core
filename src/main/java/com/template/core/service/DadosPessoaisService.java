package com.template.core.service;

import com.template.core.entity.DadosPessoais;
import com.template.core.repository.DadosPessoaisRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

/**
 * Serviço para manipulação de dados pessoais.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class DadosPessoaisService {

    private final DadosPessoaisRepository repository;

    /**
     * Busca os dados pessoais pelo ID.
     *
     * @param id o ID dos dados pessoais a ser buscado
     * @return os dados pessoais encontrados
     * @throws EntityNotFoundException se os dados pessoais não forem encontrados
     */
    public DadosPessoais findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Dados pessoais não encontrados"));
    }

    /**
     * Retorna uma página de dados pessoais.
     *
     * @param pageable informações de paginação
     * @return a página de dados pessoais
     */
    public Page<DadosPessoais> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Salva os dados pessoais.
     *
     * @param dadosPessoais os dados pessoais a serem salvos
     * @return os dados pessoais salvos
     * @throws IllegalStateException se os dados pessoais já possui um ID atribuído
     */
    public DadosPessoais save(DadosPessoais dadosPessoais) {
        if (dadosPessoais.getId() == null) {
            return repository.save(dadosPessoais);
        }

        throw new IllegalStateException("Entidade já possui um ID, utilizar a requisição de update.");
    }

    /**
     * Atualiza os dados pessoais.
     *
     * @param dadosPessoais o dados pessoais a serem atualizados
     * @return os dados pessoais atualizados
     * @throws EntityNotFoundException se os dados pessoais não forem encontrado
     */
    public DadosPessoais update(DadosPessoais dadosPessoais) {
        if (repository.findById(dadosPessoais.getId()).isPresent()) {
            return repository.save(dadosPessoais);
        }

        throw new EntityNotFoundException(MessageFormat.format("Dados pessoais com ID {0} não encontrado.", dadosPessoais.getId()));
    }

    /**
     * Exclui os dados pessoais pelo ID.
     *
     * @param id o ID dos dados pessoais a serem excluídos
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /**
     * Adiciona os dados pessoais do ADMIN no sistema.
     */
    public void addDadosPessoaisAdmin() {
        if (repository.count() == 0) {
            repository.save(new DadosPessoais(null, "NOME DO ADMIN", "00000000000", true));
        }
    }

}
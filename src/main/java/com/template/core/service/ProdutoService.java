package com.template.core.service;

import com.template.core.entity.Produto;
import com.template.core.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

/**
 * Serviço para manipulação de produtos.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository repository;

    /**
     * Busca um produto pelo ID.
     *
     * @param id o ID do produto a ser buscado
     * @return o produto encontrado
     * @throws EntityNotFoundException se o produto não for encontrado
     */
    public Produto findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    }

    /**
     * Retorna uma página de produtos.
     *
     * @param pageable informações de paginação
     * @return a página de produtos
     */
    public Page<Produto> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    /**
     * Salva um produto.
     *
     * @param produto o produto a ser salvo
     * @return o produto salvo
     * @throws IllegalStateException se o produto já possui um ID atribuído
     */
    public Produto save(Produto produto) {
        if (produto.getId() == null) {
            return repository.save(produto);
        }

        throw new IllegalStateException("Entidade já possui um ID, utilizar a requisição de update.");
    }

    /**
     * Atualiza um produto.
     *
     * @param produto o produto a ser atualizado
     * @return o produto atualizado
     * @throws EntityNotFoundException se o produto não for encontrado
     */
    public Produto update(Produto produto) {
        if (repository.findById(produto.getId()).isPresent()) {
            return repository.save(produto);
        }

        throw new EntityNotFoundException(MessageFormat.format("Produto com ID {0} não encontrado.", produto.getId()));
    }

    /**
     * Exclui um produto pelo ID.
     *
     * @param id o ID do produto a ser excluído
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

package com.template.core.test;

import com.template.core.entity.Produto;
import com.template.core.repository.ProdutoRepository;
import com.template.core.service.ProdutoService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class ProdutoTests {

    @Autowired
    private ProdutoRepository repository;

    private ProdutoService service;

    @BeforeEach
    void setup() {
        service = new ProdutoService(repository);
    }

    private Produto saveProdutoTeste() {
        return service.save(new Produto(null, "DESCRIÇÃO TESTE"));
    }

    @Test
    void findById_success() {
        Produto savedProduto = this.saveProdutoTeste();
        Produto produto = service.findById(savedProduto.getId());

        Assertions.assertEquals(savedProduto.getId(), produto.getId());
    }

    @Test
    void findById_nonExistentId() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(0L));
    }

    @Test
    void findById_nullId() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.findById(null));
    }

    @Test
    void findAll_success() {
        Assertions.assertTrue(service.findAll(PageRequest.of(0, 100)).getTotalElements() >= 0);
    }

    @Test
    void findAll_withoutPageable() {
        Assertions.assertThrows(NullPointerException.class, () -> service.findAll(null));
    }

    @Test
    void save_success() {
        Assertions.assertNotNull(this.saveProdutoTeste());
    }

    @Test
    void save_withId() {
        Produto produto = this.saveProdutoTeste();
        Assertions.assertThrows(IllegalStateException.class, () -> service.save(produto));
    }

    @Test
    void save_invalidParams() {
        Produto produto = new Produto(null, "");
        Assertions.assertThrows(ConstraintViolationException.class, () -> service.save(produto));
    }

    @Test
    void save_sameParams() {
        Assertions.assertNotNull(this.saveProdutoTeste());
        Assertions.assertThrows(DataIntegrityViolationException.class, this::saveProdutoTeste);
    }

    @Test
    void update_success() {
        Produto produto = this.saveProdutoTeste();
        produto.setDescricao("DESCRIÇÃO TESTE UPDATE");
        service.update(produto);

        Produto produtoFound = service.findById(produto.getId());
        Assertions.assertEquals(produto.getDescricao(), produtoFound.getDescricao());
    }

    @Test
    void update_invalidId() {
        Produto produto = this.saveProdutoTeste();
        produto.setId(0L);
        Assertions.assertThrows(EntityNotFoundException.class, () -> service.update(produto));
    }

    @Test
    void delete_success() {
        Long produtoId = this.saveProdutoTeste().getId();
        service.deleteById(produtoId);

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.findById(produtoId));
    }

    @Test
    void delete_invalidId() {
        Assertions.assertThrows(InvalidDataAccessApiUsageException.class, () -> service.deleteById(null));
    }

}
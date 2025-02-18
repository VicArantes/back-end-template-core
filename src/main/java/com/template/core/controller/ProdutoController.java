package com.template.core.controller;

import com.template.core.dto.PageRequestDTO;
import com.template.core.entity.Produto;
import com.template.core.service.ProdutoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para lidar com as solicitações relacionadas a produtos.
 */
@RequestMapping("/api/produto")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class ProdutoController {
    private final ProdutoService service;

    /**
     * Obtém um produto pelo seu ID.
     *
     * @param id O ID do produto a ser encontrado.
     * @return O produto correspondente ao ID fornecido.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Produto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Obtém uma página de produtos.
     *
     * @param pageRequestDTO Objeto contendo as especificações de paginação
     * @return Uma página de produtos.
     */
    @PostMapping("/find")
    public ResponseEntity<Page<Produto>> findAll(@Valid @RequestBody PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size())));
    }

    /**
     * Salva um produto.
     *
     * @param produto O produto a ser salvo.
     * @return O produto salvo.
     */
    @PostMapping("/save")
    public ResponseEntity<Produto> save(@Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(service.save(produto));
    }

    /**
     * Atualiza um produto.
     *
     * @param produto produto a ser atualizado.
     * @return produto atualizado.
     */
    @PutMapping("/update")
    public ResponseEntity<Produto> update(@Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(service.update(produto));
    }

    /**
     * Exclui um produto pelo seu ID.
     *
     * @param id O ID do produto a ser excluído.
     * @return ResponseEntity<Void> status 204 NO_CONTENT.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
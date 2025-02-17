package com.template.core.controller;

import com.template.core.dto.PageRequestDTO;
import com.template.core.entity.Permissao;
import com.template.core.service.PermissaoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para lidar com as solicitações relacionadas a permissões.
 */
@RequestMapping("/api/permissao")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class PermissaoController {
    private final PermissaoService service;

    /**
     * Obtém uma permissão pelo seu ID.
     *
     * @param id O ID da permissão a ser encontrada.
     * @return A permissão correspondente ao ID fornecido.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Permissao> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Obtém uma página de permissões.
     *
     * @param pageRequestDTO Objeto contendo as especificações de paginação
     * @return Uma página de permissões.
     */
    @PostMapping("/find")
    public ResponseEntity<Page<Permissao>> findAll(@Valid @RequestBody PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size())));
    }

    /**
     * Salva uma permissão.
     *
     * @param permissao A permissão a ser salva.
     * @return A permissão salva.
     */
    @PostMapping("/save")
    public ResponseEntity<Permissao> save(@Valid @RequestBody Permissao permissao) {
        return ResponseEntity.ok(service.save(permissao));
    }

    /**
     * Atualiza uma permissão.
     *
     * @param permissao permissão a ser atualizada.
     * @return permissão atualizada.
     */
    @PutMapping("/update")
    public ResponseEntity<Permissao> update(@Valid @RequestBody Permissao permissao) {
        return ResponseEntity.ok(service.update(permissao));
    }

    /**
     * Exclui uma permissão pelo seu ID.
     *
     * @param id O ID da permissão a ser excluída.
     * @return ResponseEntity<Void> status 204 NO_CONTENT.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
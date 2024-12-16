package com.template.core.controller;

import com.template.core.dto.PageRequestDTO;
import com.template.core.entity.Authority;
import com.template.core.service.AuthorityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para lidar com as solicitações relacionadas a authorities.
 */
@RequestMapping("/api/authority")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class AuthorityController {
    private final AuthorityService service;

    /**
     * Obtém uma authority pelo seu ID.
     *
     * @param id O ID da authority a ser encontrada.
     * @return A authority correspondente ao ID fornecido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Authority> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Obtém uma página de authorities.
     *
     * @param pageRequestDTO Objeto contendo as especificações de paginação
     * @return Uma página de authorities.
     */
    @PostMapping("/find")
    public ResponseEntity<Page<Authority>> findAll(@Valid @RequestBody PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size())));
    }

    /**
     * Salva uma authority.
     *
     * @param authority authority a ser salva.
     * @return authority salva.
     */
    @PostMapping("/save")
    public ResponseEntity<Authority> save(@Valid @RequestBody Authority authority) {
        return ResponseEntity.ok(service.save(authority));
    }

    /**
     * Atualiza uma authority.
     *
     * @param authority authority a ser atualizada.
     * @return authority atualizada.
     */
    @PutMapping("/update")
    public ResponseEntity<Authority> update(@Valid @RequestBody Authority authority) {
        return ResponseEntity.ok(service.update(authority));
    }

    /**
     * Exclui uma authority pelo seu ID.
     *
     * @param id O ID da authority a ser excluído.
     * @return ResponseEntity<Void> status 204 NO CONTENT.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
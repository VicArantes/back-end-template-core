package com.template.core.controller;

import com.template.core.dto.PageRequestDTO;
import com.template.core.entity.Role;
import com.template.core.service.RoleService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para lidar com as solicitações relacionadas a roles.
 */
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class RoleController {
    private final RoleService service;

    /**
     * Obtém uma role pelo seu ID.
     *
     * @param id O ID da role a ser encontrada.
     * @return A role correspondente ao ID fornecido.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<Role> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Obtém uma página de roles.
     *
     * @param pageRequestDTO Objeto contendo as especificações de paginação
     * @return Uma página de roles.
     */
    @PostMapping("/find")
    public ResponseEntity<Page<Role>> findAll(@Valid @RequestBody PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size())));
    }

    /**
     * Salva uma role.
     *
     * @param role role a ser salva.
     * @return role salva.
     */
    @PostMapping("/save")
    public ResponseEntity<Role> save(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(service.save(role));
    }

    /**
     * Atualiza uma role.
     *
     * @param role role a ser atualizada.
     * @return role atualizada.
     */
    @PutMapping("/update")
    public ResponseEntity<Role> update(@Valid @RequestBody Role role) {
        return ResponseEntity.ok(service.update(role));
    }

    /**
     * Exclui uma role pelo seu ID.
     *
     * @param id O ID da role a ser excluída.
     * @return ResponseEntity<Void> status 204 NO CONTENT.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
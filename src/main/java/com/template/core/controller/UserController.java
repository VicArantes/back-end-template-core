package com.template.core.controller;

import com.template.core.dto.PageRequestDTO;
import com.template.core.entity.User;
import com.template.core.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para lidar com as solicitações relacionadas a users.
 */
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class UserController {
    private final UserService service;

    /**
     * Obtém um user pelo seu ID.
     *
     * @param id O ID do user a ser encontrado.
     * @return O user correspondente ao ID fornecido.
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Obtém uma página de users.
     *
     * @param pageRequestDTO Objeto contendo as especificações de paginação
     * @return Uma página de users.
     */
    @PostMapping("/find")
    public ResponseEntity<Page<User>> findAll(@Valid @RequestBody PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size())));
    }

    /**
     * Salva um user.
     *
     * @param user O user a ser salvo.
     * @return O user salvo.
     */
    @PostMapping("/save")
    public ResponseEntity<User> save(@Valid @RequestBody User user) {
        return ResponseEntity.ok(service.save(user));
    }

    /**
     * Atualiza um user.
     *
     * @param user user a ser atualizado.
     * @return user atualizado.
     */
    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.ok(service.update(user));
    }

    /**
     * Exclui um user pelo seu ID.
     *
     * @param id O ID do user a ser excluído.
     * @return ResponseEntity<Void> status 204 NO CONTENT.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
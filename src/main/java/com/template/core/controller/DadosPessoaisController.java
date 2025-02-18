package com.template.core.controller;

import com.template.core.dto.PageRequestDTO;
import com.template.core.entity.DadosPessoais;
import com.template.core.service.DadosPessoaisService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para lidar com as solicitações relacionadas aos dados pessoais.
 */
@RequestMapping("/api/dados_pessoais")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class DadosPessoaisController {
    private final DadosPessoaisService service;

    /**
     * Obtém os dados pessoais pelo seu ID.
     *
     * @param id O ID dos dados pessoais a serem encontrados.
     * @return Os dados pessoais correspondentes ao ID fornecido.
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<DadosPessoais> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Obtém uma página de dados pessoais.
     *
     * @param pageRequestDTO Objeto contendo as especificações de paginação
     * @return Uma página de dados pessoais.
     */
    @PostMapping("/find")
    public ResponseEntity<Page<DadosPessoais>> findAll(@Valid @RequestBody PageRequestDTO pageRequestDTO) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size())));
    }

    /**
     * Salva os dados pessoais.
     *
     * @param dadosPessoais Os dados pessoais a serem salvos.
     * @return Os dados pessoais salvos.
     */
    @PostMapping("/save")
    public ResponseEntity<DadosPessoais> save(@Valid @RequestBody DadosPessoais dadosPessoais) {
        return ResponseEntity.ok(service.save(dadosPessoais));
    }

    /**
     * Atualiza os dados pessoais.
     *
     * @param dadosPessoais dadosPessoais a serem atualizados.
     * @return os dados pessoais atualizados.
     */
    @PutMapping("/update")
    public ResponseEntity<DadosPessoais> update(@Valid @RequestBody DadosPessoais dadosPessoais) {
        return ResponseEntity.ok(service.update(dadosPessoais));
    }

    /**
     * Exclui os dados pessoais pelo seu ID.
     *
     * @param id O ID dos dados pessoais a serem excluídos.
     * @return ResponseEntity<Void> status 204 NO_CONTENT.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
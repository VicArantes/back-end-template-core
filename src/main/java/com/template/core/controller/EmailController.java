package com.template.core.controller;

import com.template.core.dto.EmailDTO;
import com.template.core.service.EmailService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para lidar com as solicitações relacionadas a emails.
 */
@RequestMapping("/api/email")
@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearer-key")
public class EmailController {
    private final EmailService service;

    /**
     * Envia um email.
     *
     * @param email Objeto contendo o email do destinatário
     * @return ResponseEntity<Void> status 204 NO_CONTENT.
     */
    @PostMapping("/send-email")
    public ResponseEntity<Void> sendEmail(@RequestBody @Valid EmailDTO email) throws MessagingException {
        service.sendEmail(email);
        return ResponseEntity.noContent().build();
    }

}
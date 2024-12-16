package com.template.core.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record que representa um EmailDTO.
 *
 * @param email Email do destinatário.
 */
public record EmailDTO(@NotBlank String email) {

}

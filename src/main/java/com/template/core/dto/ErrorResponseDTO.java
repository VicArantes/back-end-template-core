package com.template.core.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Record que representa um ErrorResponseDTO.
 *
 * @param message Message do ErrorResponseDTO.
 */
public record ErrorResponseDTO(@NotBlank String message) {

}

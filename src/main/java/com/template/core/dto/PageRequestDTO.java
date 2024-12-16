package com.template.core.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Record que representa um PageRequestDTO.
 *
 * @param page Page do PageRequestDTO.
 * @param size Size do PageRequestDTO.
 */
public record PageRequestDTO(@NotNull Integer page, @NotNull Integer size) {

}

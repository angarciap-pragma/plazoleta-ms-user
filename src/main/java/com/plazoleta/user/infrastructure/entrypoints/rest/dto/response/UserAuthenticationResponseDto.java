package com.plazoleta.user.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP interna para autenticación de usuarios.
 */
public record UserAuthenticationResponseDto(
        Long id,
        String email,
        String password,
        String role,
        boolean active
) {
}

package com.plazoleta.user.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP de consulta de usuario.
 */
public record UserDetailsResponseDto(
        Long id,
        String firstName,
        String lastName,
        String documentId,
        String phoneNumber,
        String email,
        String role,
        Long restaurantId
) {
}

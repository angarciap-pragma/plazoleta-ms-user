package com.plazoleta.user.infrastructure.entrypoints.rest.dto.response;

/**
 * Representa la respuesta HTTP de creación de usuario.
 */
public record UserCreatedResponseDto(
        Long id,
        String firstName,
        String lastName,
        String documentId,
        String phoneNumber,
        String birthDate,
        String email,
        String role,
        Long restaurantId
) {
}

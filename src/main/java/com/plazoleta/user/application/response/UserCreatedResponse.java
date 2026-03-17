package com.plazoleta.user.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna de creación de usuario.
 */
@Builder
public record UserCreatedResponse(
        Long id,
        String firstName,
        String lastName,
        String documentId,
        String phoneNumber,
        String birthDate,
        String email,
        String role
) {
}

package com.plazoleta.user.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna de consulta de usuario.
 */
@Builder
public record UserDetailsResponse(
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

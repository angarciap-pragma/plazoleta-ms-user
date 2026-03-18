package com.plazoleta.user.application.response;

import lombok.Builder;

/**
 * Representa la respuesta interna con datos necesarios para autenticación.
 */
@Builder
public record UserAuthenticationResponse(
        Long id,
        String email,
        String password,
        String role,
        boolean active
) {
}

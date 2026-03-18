package com.plazoleta.user.application.query;

/**
 * Representa la consulta interna para autenticación por correo.
 */
public record GetUserAuthenticationByEmailQuery(String email) {
}

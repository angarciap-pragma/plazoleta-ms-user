package com.plazoleta.user.application.command;

/**
 * Representa la solicitud interna para crear un cliente.
 */
public record CreateCustomerCommand(
        String firstName,
        String lastName,
        String documentId,
        String phoneNumber,
        String email,
        Long roleId,
        String password
) {
}

package com.plazoleta.user.application.command;

/**
 * Representa la solicitud interna para crear un empleado.
 */
public record CreateEmployeeCommand(
        String firstName,
        String lastName,
        String documentId,
        String phoneNumber,
        String email,
        Long roleId,
        String password,
        Long restaurantId,
        Long ownerId
) {
}

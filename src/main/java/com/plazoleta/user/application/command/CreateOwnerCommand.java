package com.plazoleta.user.application.command;

import java.time.LocalDate;

/**
 * Representa la solicitud interna para crear un propietario.
 */
public record CreateOwnerCommand(
        String firstName,
        String lastName,
        String documentId,
        String phoneNumber,
        LocalDate birthDate,
        String email,
        String password
) {
}

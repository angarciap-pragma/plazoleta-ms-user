package com.plazoleta.user.infrastructure.entrypoints.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Representa el cuerpo HTTP para crear un cliente.
 */
public record CreateCustomerRequestDto(
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        @NotBlank(message = "documentId is required")
        @Pattern(regexp = "^[0-9]+$", message = "documentId must contain only digits")
        String documentId,
        @NotBlank(message = "phoneNumber is required")
        @Size(max = 13, message = "phoneNumber must contain at most 13 characters")
        @Pattern(regexp = "^\\+?[0-9]+$", message = "phoneNumber must contain only digits and optional leading plus sign")
        String phoneNumber,
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid email address")
        String email,
        @NotNull(message = "roleId is required")
        Long roleId,
        @NotBlank(message = "password is required")
        String password
) {
}

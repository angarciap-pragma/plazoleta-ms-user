package com.plazoleta.user.infrastructure.drivenadapters.rest.dto;

/**
 * Representa la respuesta remota mínima de restaurante para validación de propiedad.
 */
public record RestaurantOwnershipResponseDto(
        Long id,
        Long ownerId
) {
}

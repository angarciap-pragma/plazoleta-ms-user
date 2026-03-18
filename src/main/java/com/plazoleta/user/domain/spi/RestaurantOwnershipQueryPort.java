package com.plazoleta.user.domain.spi;

/**
 * Define la consulta de propiedad de restaurantes para validar creación de empleados.
 */
public interface RestaurantOwnershipQueryPort {

    boolean isOwnerOfRestaurant(Long ownerId, Long restaurantId);
}

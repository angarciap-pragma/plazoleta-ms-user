package com.plazoleta.user.infrastructure.drivenadapters.rest.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Representa la configuración del cliente HTTP hacia ms-restaurant.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "user.clients.restaurant-service")
public class RestaurantServiceProperties {

    private String baseUrl;
}

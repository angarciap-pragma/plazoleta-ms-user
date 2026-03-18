package com.plazoleta.user.infrastructure.drivenadapters.rest;

import com.plazoleta.user.domain.spi.RestaurantOwnershipQueryPort;
import com.plazoleta.user.infrastructure.drivenadapters.rest.dto.RestaurantOwnershipResponseDto;
import com.plazoleta.user.infrastructure.drivenadapters.rest.properties.RestaurantServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/**
 * Consulta ms-restaurant para validar la propiedad de un restaurante.
 */
@Component
@RequiredArgsConstructor
public class RestaurantOwnershipRestAdapter implements RestaurantOwnershipQueryPort {

    private final RestClient.Builder restClientBuilder;
    private final RestaurantServiceProperties restaurantServiceProperties;

    @Override
    public boolean isOwnerOfRestaurant(final Long ownerId, final Long restaurantId) {
        try {
            RestaurantOwnershipResponseDto response = restClientBuilder.build()
                    .get()
                    .uri(restaurantServiceProperties.getBaseUrl() + "/restaurants/internal/{id}", restaurantId)
                    .retrieve()
                    .body(RestaurantOwnershipResponseDto.class);
            return response != null && ownerId.equals(response.ownerId());
        } catch (HttpClientErrorException exception) {
            return false;
        }
    }
}

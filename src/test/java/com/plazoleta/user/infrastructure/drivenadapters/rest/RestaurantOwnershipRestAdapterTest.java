package com.plazoleta.user.infrastructure.drivenadapters.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.user.infrastructure.drivenadapters.rest.dto.RestaurantOwnershipResponseDto;
import com.plazoleta.user.infrastructure.drivenadapters.rest.properties.RestaurantServiceProperties;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

class RestaurantOwnershipRestAdapterTest {

    private final RestClient.Builder restClientBuilder = Mockito.mock(RestClient.Builder.class);
    private final RestClient restClient = Mockito.mock(RestClient.class);
    private final RestClient.RequestHeadersUriSpec<?> requestHeadersUriSpec = Mockito.mock(RestClient.RequestHeadersUriSpec.class);
    private final RestClient.ResponseSpec responseSpec = Mockito.mock(RestClient.ResponseSpec.class);
    private final RestaurantServiceProperties properties = new RestaurantServiceProperties();
    private final RestaurantOwnershipRestAdapter adapter = new RestaurantOwnershipRestAdapter(restClientBuilder, properties);

    RestaurantOwnershipRestAdapterTest() {
        properties.setBaseUrl("http://localhost:8083");
    }

    @Test
    @DisplayName("should return true when owner matches restaurant")
    void shouldReturnTrueWhenOwnerMatchesRestaurant() {
        mockSuccessfulResponse(new RestaurantOwnershipResponseDto(1L, 2L));

        assertThat(adapter.isOwnerOfRestaurant(2L, 1L)).isTrue();
    }

    @Test
    @DisplayName("should return false when owner does not match restaurant")
    void shouldReturnFalseWhenOwnerDoesNotMatchRestaurant() {
        mockSuccessfulResponse(new RestaurantOwnershipResponseDto(1L, 3L));

        assertThat(adapter.isOwnerOfRestaurant(2L, 1L)).isFalse();
    }

    @Test
    @DisplayName("should return false when remote response is null")
    void shouldReturnFalseWhenRemoteResponseIsNull() {
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8083/restaurants/internal/{id}", 1L))
                .thenReturn((RestClient.RequestHeadersSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RestaurantOwnershipResponseDto.class)).thenReturn(null);

        assertThat(adapter.isOwnerOfRestaurant(2L, 1L)).isFalse();
    }

    @Test
    @DisplayName("should return false when remote service returns not found")
    void shouldReturnFalseWhenRemoteServiceReturnsNotFound() {
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8083/restaurants/internal/{id}", 1L))
                .thenReturn((RestClient.RequestHeadersSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RestaurantOwnershipResponseDto.class))
                .thenThrow(HttpClientErrorException.NotFound.create(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Not found",
                        org.springframework.http.HttpHeaders.EMPTY,
                        new byte[0],
                        StandardCharsets.UTF_8
                ));

        assertThat(adapter.isOwnerOfRestaurant(2L, 1L)).isFalse();
    }

    private void mockSuccessfulResponse(final RestaurantOwnershipResponseDto response) {
        when(restClientBuilder.build()).thenReturn(restClient);
        when(restClient.get()).thenReturn((RestClient.RequestHeadersUriSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8083/restaurants/internal/{id}", 1L))
                .thenReturn((RestClient.RequestHeadersSpec) requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(RestaurantOwnershipResponseDto.class)).thenReturn(response);
    }
}

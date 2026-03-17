package com.plazoleta.user.infrastructure.entrypoints.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.request.CreateOwnerRequestDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserDetailsResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserCreatedResponseDto;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(
        properties = "spring.profiles.active=test",
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class UserControllerTest {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("should create owner successfully")
    void shouldCreateOwnerSuccessfully() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "123456789",
                "+573005698325",
                LocalDate.parse("1990-01-01"),
                "owner@plazoleta.com",
                "Admin123*"
        );

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto);
        UserCreatedResponseDto body = objectMapper.readValue(response.body(), UserCreatedResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body.id()).isNotNull();
        assertThat(body.email()).isEqualTo("owner@plazoleta.com");
        assertThat(body.role()).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("should reject duplicated email")
    void shouldRejectDuplicatedEmail() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "123456780",
                "+573005698320",
                LocalDate.parse("1990-01-01"),
                "owner-duplicated@plazoleta.com",
                "Admin123*"
        );

        sendCreateOwnerRequest(requestDto);
        HttpResponse<String> response = sendCreateOwnerRequest(requestDto);
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(body).containsEntry("code", "USER_409_EMAIL_ALREADY_EXISTS");
    }

    @Test
    @DisplayName("should reject underage owner")
    void shouldRejectUnderageOwner() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "123456781",
                "+573005698321",
                LocalDate.now().minusYears(17),
                "underage@plazoleta.com",
                "Admin123*"
        );

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto);
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body).containsEntry("code", "USER_400_UNDERAGE_OWNER");
    }

    @Test
    @DisplayName("should reject invalid phone format")
    void shouldRejectInvalidPhoneFormat() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "123456782",
                "invalid-phone",
                LocalDate.parse("1990-01-01"),
                "invalid-phone@plazoleta.com",
                "Admin123*"
        );

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto);
        Map<String, Object> body = objectMapper.readValue(response.body(), new TypeReference<>() {
        });

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.get("code")).isEqualTo("COMMON_400");
    }

    @Test
    @DisplayName("should get user by id")
    void shouldGetUserById() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "223344556",
                "+573005698322",
                LocalDate.parse("1990-01-01"),
                "get-owner@plazoleta.com",
                "Admin123*"
        );

        HttpResponse<String> createResponse = sendCreateOwnerRequest(requestDto);
        UserCreatedResponseDto created = objectMapper.readValue(createResponse.body(), UserCreatedResponseDto.class);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users/" + created.id()))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        UserDetailsResponseDto body = objectMapper.readValue(response.body(), UserDetailsResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.role()).isEqualTo("OWNER");
    }

    private HttpResponse<String> sendCreateOwnerRequest(final CreateOwnerRequestDto requestDto) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users/owners"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestDto)))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}

package com.plazoleta.user.infrastructure.entrypoints.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.request.CreateCustomerRequestDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.request.CreateEmployeeRequestDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.request.CreateOwnerRequestDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserAuthenticationResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserDetailsResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserCreatedResponseDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import javax.crypto.SecretKey;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "user.clients.restaurant-service.base-url=http://localhost:18083"
})
class UserControllerTest {

    private static final String JWT_SECRET = "this-is-a-shared-secret-key-with-safe-length-123456";
    private static final String JWT_ISSUER = "plazoleta-auth";
    private static HttpServer restaurantStubServer;

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeAll
    static void setUpRestaurantStub() throws IOException {
        restaurantStubServer = HttpServer.create(new java.net.InetSocketAddress(18083), 0);
        restaurantStubServer.createContext("/restaurants/internal/1", exchange -> {
            byte[] response = "{\"id\":1,\"ownerId\":2}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpStatus.OK.value(), response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        restaurantStubServer.createContext("/restaurants/internal/9", exchange -> {
            byte[] response = "{\"id\":9,\"ownerId\":9}".getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(HttpStatus.OK.value(), response.length);
            exchange.getResponseBody().write(response);
            exchange.close();
        });
        restaurantStubServer.start();
    }

    @AfterAll
    static void tearDownRestaurantStub() {
        if (restaurantStubServer != null) {
            restaurantStubServer.stop(0);
        }
    }

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

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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

        sendCreateOwnerRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
        HttpResponse<String> response = sendCreateOwnerRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
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

        HttpResponse<String> createResponse = sendCreateOwnerRequest(requestDto, buildToken(1L, "admin@plazoleta.com", "ADMIN"));
        UserCreatedResponseDto created = objectMapper.readValue(createResponse.body(), UserCreatedResponseDto.class);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users/" + created.id()))
                .header("Authorization", "Bearer " + buildToken(1L, "admin@plazoleta.com", "ADMIN"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        UserDetailsResponseDto body = objectMapper.readValue(response.body(), UserDetailsResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.role()).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("should expose internal authentication lookup without jwt")
    void shouldExposeInternalAuthenticationLookupWithoutJwt() throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users/internal/authentication?email=admin@plazoleta.com"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        UserAuthenticationResponseDto body = objectMapper.readValue(response.body(), UserAuthenticationResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.role()).isEqualTo("ADMIN");
        assertThat(body.password()).isNotBlank();
    }

    @Test
    @DisplayName("should expose internal user lookup without jwt")
    void shouldExposeInternalUserLookupWithoutJwt() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "323344556",
                "+573005698324",
                LocalDate.parse("1990-01-01"),
                "internal-owner@plazoleta.com",
                "Admin123*"
        );
        HttpResponse<String> createResponse = sendCreateOwnerRequest(
                requestDto,
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );
        UserCreatedResponseDto created = objectMapper.readValue(createResponse.body(), UserCreatedResponseDto.class);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/users/internal/" + created.id()))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        UserDetailsResponseDto body = objectMapper.readValue(response.body(), UserDetailsResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(body.email()).isEqualTo("internal-owner@plazoleta.com");
    }

    @Test
    @DisplayName("should reject owner creation when caller is not admin")
    void shouldRejectOwnerCreationWhenCallerIsNotAdmin() throws Exception {
        CreateOwnerRequestDto requestDto = new CreateOwnerRequestDto(
                "Andrea",
                "Garcia",
                "123456783",
                "+573005698323",
                LocalDate.parse("1990-01-01"),
                "forbidden-owner@plazoleta.com",
                "Admin123*"
        );

        HttpResponse<String> response = sendCreateOwnerRequest(requestDto, buildToken(2L, "owner@plazoleta.com", "OWNER"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("should create employee when caller owns restaurant")
    void shouldCreateEmployeeWhenCallerOwnsRestaurant() throws Exception {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto(
                "John",
                "Cook",
                "456789123",
                "+573005698329",
                "employee@plazoleta.com",
                3L,
                "Employee123*",
                1L
        );

        HttpResponse<String> response = sendPostRequest(
                "/users/employees",
                requestDto,
                buildToken(2L, "owner@plazoleta.com", "OWNER")
        );
        UserCreatedResponseDto body = objectMapper.readValue(response.body(), UserCreatedResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body.role()).isEqualTo("EMPLOYEE");
        assertThat(body.restaurantId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should reject employee creation when owner does not own restaurant")
    void shouldRejectEmployeeCreationWhenOwnerDoesNotOwnRestaurant() throws Exception {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto(
                "Jane",
                "Cook",
                "456789124",
                "+573005698330",
                "employee-forbidden@plazoleta.com",
                3L,
                "Employee123*",
                9L
        );

        HttpResponse<String> response = sendPostRequest(
                "/users/employees",
                requestDto,
                buildToken(2L, "owner@plazoleta.com", "OWNER")
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    @DisplayName("should create customer without jwt")
    void shouldCreateCustomerWithoutJwt() throws Exception {
        CreateCustomerRequestDto requestDto = new CreateCustomerRequestDto(
                "Customer",
                "Plazoleta",
                "789456123",
                "+573005698331",
                "customer@plazoleta.com",
                4L,
                "Customer123*"
        );

        HttpResponse<String> response = sendPostRequest("/users/customers", requestDto, null);
        UserCreatedResponseDto body = objectMapper.readValue(response.body(), UserCreatedResponseDto.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(body.role()).isEqualTo("CUSTOMER");
        assertThat(body.restaurantId()).isNull();
    }

    @Test
    @DisplayName("should reject employee creation when caller is not owner")
    void shouldRejectEmployeeCreationWhenCallerIsNotOwner() throws Exception {
        CreateEmployeeRequestDto requestDto = new CreateEmployeeRequestDto(
                "John",
                "Cook",
                "456789125",
                "+573005698332",
                "employee-role@plazoleta.com",
                3L,
                "Employee123*",
                1L
        );

        HttpResponse<String> response = sendPostRequest(
                "/users/employees",
                requestDto,
                buildToken(1L, "admin@plazoleta.com", "ADMIN")
        );

        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    private HttpResponse<String> sendCreateOwnerRequest(final CreateOwnerRequestDto requestDto, final String token) throws Exception {
        return sendPostRequest("/users/owners", requestDto, token);
    }

    private HttpResponse<String> sendPostRequest(final String path, final Object requestDto, final String token) throws Exception {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + path))
                .header("Content-Type", "application/json");

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        HttpRequest request = builder
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestDto)))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String buildToken(final Long userId, final String email, final String role) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        SecretKey secretKey = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(email)
                .issuer(JWT_ISSUER)
                .issuedAt(java.util.Date.from(now.toInstant()))
                .expiration(java.util.Date.from(now.plusMinutes(30).toInstant()))
                .claim("userId", userId)
                .claim("email", email)
                .claim("role", role)
                .signWith(secretKey)
                .compact();
    }
}

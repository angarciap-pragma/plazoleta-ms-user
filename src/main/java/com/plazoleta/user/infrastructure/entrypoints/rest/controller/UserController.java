package com.plazoleta.user.infrastructure.entrypoints.rest.controller;

import com.plazoleta.user.infrastructure.entrypoints.rest.dto.request.CreateOwnerRequestDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserDetailsResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserCreatedResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.mapper.UserRestMapper;
import com.plazoleta.user.infrastructure.service.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Expone los endpoints HTTP del microservicio de usuarios.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for user management")
public class UserController {

    private final UserHandler userHandler;
    private final UserRestMapper userRestMapper;

    @PostMapping("/owners")
    @Operation(
            summary = "Create owner user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Owner created"),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request",
                            content = @Content(schema = @Schema(implementation = Object.class))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Duplicated owner data",
                            content = @Content(
                                    examples = @ExampleObject(
                                            value = "{\"code\":\"USER_409_EMAIL_ALREADY_EXISTS\",\"message\":\"Email already exists\",\"status\":409,\"path\":\"/users/owners\",\"timestamp\":\"2026-03-17T10:00:00Z\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<UserCreatedResponseDto> createOwner(
            @Valid @RequestBody final CreateOwnerRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRestMapper.toDto(userHandler.createOwner(userRestMapper.toCommand(requestDto))));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    public ResponseEntity<UserDetailsResponseDto> getUserById(@PathVariable final Long id) {
        return ResponseEntity.ok(userRestMapper.toDto(userHandler.getUserById(id)));
    }
}

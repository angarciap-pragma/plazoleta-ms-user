package com.plazoleta.user.infrastructure.entrypoints.rest.mapper;

import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.response.UserAuthenticationResponse;
import com.plazoleta.user.application.response.UserDetailsResponse;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.request.CreateOwnerRequestDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserAuthenticationResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserDetailsResponseDto;
import com.plazoleta.user.infrastructure.entrypoints.rest.dto.response.UserCreatedResponseDto;
import org.mapstruct.Mapper;

/**
 * Mapea contratos HTTP hacia comandos y respuestas de aplicación.
 */
@Mapper(componentModel = "spring")
public interface UserRestMapper {

    CreateOwnerCommand toCommand(CreateOwnerRequestDto requestDto);

    UserCreatedResponseDto toDto(UserCreatedResponse response);

    UserDetailsResponseDto toDto(UserDetailsResponse response);

    UserAuthenticationResponseDto toDto(UserAuthenticationResponse response);
}

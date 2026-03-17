package com.plazoleta.user.application.usecase;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.user.application.query.GetUserByIdQuery;
import com.plazoleta.user.application.response.UserDetailsResponse;
import com.plazoleta.user.domain.api.GetUserByIdServicePort;
import com.plazoleta.user.domain.exception.UserErrorCode;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.spi.UserPersistencePort;

/**
 * Implementa la consulta de usuarios por identificador.
 */
public class GetUserByIdUseCase implements GetUserByIdServicePort {

    private final UserPersistencePort userPersistencePort;

    public GetUserByIdUseCase(final UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public UserDetailsResponse getUserById(final GetUserByIdQuery query) {
        User user = userPersistencePort.findById(query.id())
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.USER_NOT_FOUND));

        return UserDetailsResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .documentId(user.getDocumentId())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}

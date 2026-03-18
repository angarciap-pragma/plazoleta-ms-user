package com.plazoleta.user.application.usecase;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.user.application.query.GetUserAuthenticationByEmailQuery;
import com.plazoleta.user.application.response.UserAuthenticationResponse;
import com.plazoleta.user.domain.api.GetUserAuthenticationByEmailServicePort;
import com.plazoleta.user.domain.exception.UserErrorCode;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.spi.UserPersistencePort;

/**
 * Implementa la consulta interna de usuarios por correo para autenticación.
 */
public class GetUserAuthenticationByEmailUseCase implements GetUserAuthenticationByEmailServicePort {

    private final UserPersistencePort userPersistencePort;

    public GetUserAuthenticationByEmailUseCase(final UserPersistencePort userPersistencePort) {
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public UserAuthenticationResponse getUserAuthenticationByEmail(final GetUserAuthenticationByEmailQuery query) {
        User user = userPersistencePort.findByEmail(query.email())
                .orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.USER_NOT_FOUND));

        return UserAuthenticationResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
}

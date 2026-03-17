package com.plazoleta.user.application.usecase;

import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
import com.plazoleta.user.domain.exception.UserErrorCode;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;

/**
 * Implementa el caso de uso para crear propietarios.
 */
public class CreateOwnerUseCase implements CreateOwnerServicePort {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public CreateOwnerUseCase(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort
    ) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserCreatedResponse createOwner(final CreateOwnerCommand command) {
        validateUniqueness(command);

        User savedUser = userPersistencePort.save(
                User.createOwner(
                        command.firstName(),
                        command.lastName(),
                        command.documentId(),
                        command.phoneNumber(),
                        command.birthDate(),
                        command.email(),
                        passwordEncoderPort.encode(command.password())
                )
        );

        return UserCreatedResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .documentId(savedUser.getDocumentId())
                .phoneNumber(savedUser.getPhoneNumber())
                .birthDate(savedUser.getBirthDate().toString())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    private void validateUniqueness(final CreateOwnerCommand command) {
        if (userPersistencePort.existsByEmail(command.email())) {
            throw new ConflictException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userPersistencePort.existsByDocumentId(command.documentId())) {
            throw new ConflictException(UserErrorCode.DOCUMENT_ALREADY_EXISTS);
        }
    }
}

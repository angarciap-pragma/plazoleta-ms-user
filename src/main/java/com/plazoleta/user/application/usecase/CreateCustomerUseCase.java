package com.plazoleta.user.application.usecase;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.user.application.command.CreateCustomerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateCustomerServicePort;
import com.plazoleta.user.domain.exception.UserErrorCode;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;

/**
 * Implementa el caso de uso para crear clientes.
 */
public class CreateCustomerUseCase implements CreateCustomerServicePort {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    public CreateCustomerUseCase(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort
    ) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
    }

    @Override
    public UserCreatedResponse createCustomer(final CreateCustomerCommand command) {
        validateUniqueness(command.email(), command.documentId());
        validateRole(command.roleId(), UserRole.CUSTOMER);

        User savedUser = userPersistencePort.save(User.createCustomer(
                command.firstName(),
                command.lastName(),
                command.documentId(),
                command.phoneNumber(),
                command.email(),
                passwordEncoderPort.encode(command.password())
        ));

        return UserCreatedResponse.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .documentId(savedUser.getDocumentId())
                .phoneNumber(savedUser.getPhoneNumber())
                .birthDate(null)
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .restaurantId(savedUser.getRestaurantId())
                .build();
    }

    private void validateUniqueness(final String email, final String documentId) {
        if (userPersistencePort.existsByEmail(email)) {
            throw new ConflictException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (userPersistencePort.existsByDocumentId(documentId)) {
            throw new ConflictException(UserErrorCode.DOCUMENT_ALREADY_EXISTS);
        }
    }

    private void validateRole(final Long roleId, final UserRole expectedRole) {
        if (!expectedRole.getId().equals(roleId)) {
            throw new BadRequestException(UserErrorCode.INVALID_ROLE_ID);
        }
    }
}

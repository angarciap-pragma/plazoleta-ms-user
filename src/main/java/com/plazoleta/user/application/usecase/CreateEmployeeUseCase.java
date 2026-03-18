package com.plazoleta.user.application.usecase;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.user.application.command.CreateEmployeeCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateEmployeeServicePort;
import com.plazoleta.user.domain.exception.UserErrorCode;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.RestaurantOwnershipQueryPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;

/**
 * Implementa el caso de uso para crear empleados.
 */
public class CreateEmployeeUseCase implements CreateEmployeeServicePort {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final RestaurantOwnershipQueryPort restaurantOwnershipQueryPort;

    public CreateEmployeeUseCase(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort,
            final RestaurantOwnershipQueryPort restaurantOwnershipQueryPort
    ) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncoderPort = passwordEncoderPort;
        this.restaurantOwnershipQueryPort = restaurantOwnershipQueryPort;
    }

    @Override
    public UserCreatedResponse createEmployee(final CreateEmployeeCommand command) {
        validateUniqueness(command.email(), command.documentId());
        validateRole(command.roleId(), UserRole.EMPLOYEE);

        if (!restaurantOwnershipQueryPort.isOwnerOfRestaurant(command.ownerId(), command.restaurantId())) {
            throw new ForbiddenException(UserErrorCode.OWNER_RESTAURANT_MISMATCH);
        }

        User savedUser = userPersistencePort.save(User.createEmployee(
                command.firstName(),
                command.lastName(),
                command.documentId(),
                command.phoneNumber(),
                command.email(),
                passwordEncoderPort.encode(command.password()),
                command.restaurantId()
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

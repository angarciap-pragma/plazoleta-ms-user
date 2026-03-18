package com.plazoleta.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.common.exception.ForbiddenException;
import com.plazoleta.user.application.command.CreateEmployeeCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.RestaurantOwnershipQueryPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateEmployeeUseCaseTest {

    private final UserPersistencePort userPersistencePort = Mockito.mock(UserPersistencePort.class);
    private final PasswordEncoderPort passwordEncoderPort = Mockito.mock(PasswordEncoderPort.class);
    private final RestaurantOwnershipQueryPort restaurantOwnershipQueryPort =
            Mockito.mock(RestaurantOwnershipQueryPort.class);
    private final CreateEmployeeUseCase createEmployeeUseCase = new CreateEmployeeUseCase(
            userPersistencePort,
            passwordEncoderPort,
            restaurantOwnershipQueryPort
    );

    @Test
    @DisplayName("should create employee successfully")
    void shouldCreateEmployeeSuccessfully() {
        CreateEmployeeCommand command = buildCommand(1L, 2L, 3L);
        User savedUser = User.builder()
                .id(5L)
                .firstName("John")
                .lastName("Cook")
                .documentId("456789123")
                .phoneNumber("+573005698329")
                .email("employee@plazoleta.com")
                .password("encoded")
                .role(UserRole.EMPLOYEE.name())
                .active(true)
                .restaurantId(1L)
                .build();

        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);
        when(restaurantOwnershipQueryPort.isOwnerOfRestaurant(command.ownerId(), command.restaurantId())).thenReturn(true);
        when(passwordEncoderPort.encode(command.password())).thenReturn("encoded");
        when(userPersistencePort.save(Mockito.any(User.class))).thenReturn(savedUser);

        UserCreatedResponse response = createEmployeeUseCase.createEmployee(command);

        assertThat(response.id()).isEqualTo(5L);
        assertThat(response.role()).isEqualTo("EMPLOYEE");
        assertThat(response.restaurantId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should reject invalid role id for employee")
    void shouldRejectInvalidRoleIdForEmployee() {
        CreateEmployeeCommand command = buildCommand(1L, 2L, 4L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);

        assertThatThrownBy(() -> createEmployeeUseCase.createEmployee(command))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("should reject duplicated email for employee")
    void shouldRejectDuplicatedEmailForEmployee() {
        CreateEmployeeCommand command = buildCommand(1L, 2L, 3L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(true);

        assertThatThrownBy(() -> createEmployeeUseCase.createEmployee(command))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("should reject duplicated document for employee")
    void shouldRejectDuplicatedDocumentForEmployee() {
        CreateEmployeeCommand command = buildCommand(1L, 2L, 3L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(true);

        assertThatThrownBy(() -> createEmployeeUseCase.createEmployee(command))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("should reject employee when owner does not own restaurant")
    void shouldRejectEmployeeWhenOwnerDoesNotOwnRestaurant() {
        CreateEmployeeCommand command = buildCommand(1L, 2L, 3L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);
        when(restaurantOwnershipQueryPort.isOwnerOfRestaurant(command.ownerId(), command.restaurantId())).thenReturn(false);

        assertThatThrownBy(() -> createEmployeeUseCase.createEmployee(command))
                .isInstanceOf(ForbiddenException.class);
    }

    private CreateEmployeeCommand buildCommand(final Long restaurantId, final Long ownerId, final Long roleId) {
        return new CreateEmployeeCommand(
                "John",
                "Cook",
                "456789123",
                "+573005698329",
                "employee@plazoleta.com",
                roleId,
                "Employee123*",
                restaurantId,
                ownerId
        );
    }
}

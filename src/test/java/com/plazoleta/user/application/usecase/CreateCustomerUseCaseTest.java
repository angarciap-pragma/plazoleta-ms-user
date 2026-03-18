package com.plazoleta.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.user.application.command.CreateCustomerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateCustomerUseCaseTest {

    private final UserPersistencePort userPersistencePort = Mockito.mock(UserPersistencePort.class);
    private final PasswordEncoderPort passwordEncoderPort = Mockito.mock(PasswordEncoderPort.class);
    private final CreateCustomerUseCase createCustomerUseCase =
            new CreateCustomerUseCase(userPersistencePort, passwordEncoderPort);

    @Test
    @DisplayName("should create customer successfully")
    void shouldCreateCustomerSuccessfully() {
        CreateCustomerCommand command = buildCommand(4L);
        User savedUser = User.builder()
                .id(6L)
                .firstName("Customer")
                .lastName("User")
                .documentId("789456123")
                .phoneNumber("+573005698331")
                .email("customer@plazoleta.com")
                .password("encoded")
                .role(UserRole.CUSTOMER.name())
                .active(true)
                .build();

        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);
        when(passwordEncoderPort.encode(command.password())).thenReturn("encoded");
        when(userPersistencePort.save(Mockito.any(User.class))).thenReturn(savedUser);

        UserCreatedResponse response = createCustomerUseCase.createCustomer(command);

        assertThat(response.id()).isEqualTo(6L);
        assertThat(response.role()).isEqualTo("CUSTOMER");
        assertThat(response.restaurantId()).isNull();
    }

    @Test
    @DisplayName("should reject invalid role id for customer")
    void shouldRejectInvalidRoleIdForCustomer() {
        CreateCustomerCommand command = buildCommand(2L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);

        assertThatThrownBy(() -> createCustomerUseCase.createCustomer(command))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("should reject duplicated email for customer")
    void shouldRejectDuplicatedEmailForCustomer() {
        CreateCustomerCommand command = buildCommand(4L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(true);

        assertThatThrownBy(() -> createCustomerUseCase.createCustomer(command))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("should reject duplicated document for customer")
    void shouldRejectDuplicatedDocumentForCustomer() {
        CreateCustomerCommand command = buildCommand(4L);
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(true);

        assertThatThrownBy(() -> createCustomerUseCase.createCustomer(command))
                .isInstanceOf(ConflictException.class);
    }

    private CreateCustomerCommand buildCommand(final Long roleId) {
        return new CreateCustomerCommand(
                "Customer",
                "User",
                "789456123",
                "+573005698331",
                "customer@plazoleta.com",
                roleId,
                "Customer123*"
        );
    }
}

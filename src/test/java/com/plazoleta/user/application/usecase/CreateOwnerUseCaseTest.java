package com.plazoleta.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.BadRequestException;
import com.plazoleta.common.exception.ConflictException;
import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CreateOwnerUseCaseTest {

    private final UserPersistencePort userPersistencePort = Mockito.mock(UserPersistencePort.class);
    private final PasswordEncoderPort passwordEncoderPort = Mockito.mock(PasswordEncoderPort.class);
    private final CreateOwnerUseCase createOwnerUseCase = new CreateOwnerUseCase(userPersistencePort, passwordEncoderPort);

    @Test
    @DisplayName("should create owner successfully")
    void shouldCreateOwnerSuccessfully() {
        CreateOwnerCommand command = buildCommand(LocalDate.now().minusYears(20), "owner@plazoleta.com", "123456789");
        User savedUser = User.builder()
                .id(1L)
                .firstName("Andrea")
                .lastName("Garcia")
                .documentId("123456789")
                .phoneNumber("+573005698325")
                .birthDate(command.birthDate())
                .email(command.email())
                .password("encoded")
                .role(UserRole.OWNER.name())
                .build();

        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);
        when(passwordEncoderPort.encode(command.password())).thenReturn("encoded");
        when(userPersistencePort.save(Mockito.any(User.class))).thenReturn(savedUser);

        UserCreatedResponse response = createOwnerUseCase.createOwner(command);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("owner@plazoleta.com");
        assertThat(response.role()).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("should reject duplicated email")
    void shouldRejectDuplicatedEmail() {
        CreateOwnerCommand command = buildCommand(LocalDate.now().minusYears(20), "owner@plazoleta.com", "123456789");
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(true);

        assertThatThrownBy(() -> createOwnerUseCase.createOwner(command))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("should reject duplicated document")
    void shouldRejectDuplicatedDocument() {
        CreateOwnerCommand command = buildCommand(LocalDate.now().minusYears(20), "owner@plazoleta.com", "123456789");
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(true);

        assertThatThrownBy(() -> createOwnerUseCase.createOwner(command))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    @DisplayName("should reject underage owner from domain rule")
    void shouldRejectUnderageOwnerFromDomainRule() {
        CreateOwnerCommand command = buildCommand(LocalDate.now().minusYears(17), "owner@plazoleta.com", "123456789");
        when(userPersistencePort.existsByEmail(command.email())).thenReturn(false);
        when(userPersistencePort.existsByDocumentId(command.documentId())).thenReturn(false);
        when(passwordEncoderPort.encode(command.password())).thenReturn("encoded");

        assertThatThrownBy(() -> createOwnerUseCase.createOwner(command))
                .isInstanceOf(BadRequestException.class);
    }

    private CreateOwnerCommand buildCommand(
            final LocalDate birthDate,
            final String email,
            final String documentId
    ) {
        return new CreateOwnerCommand(
                "Andrea",
                "Garcia",
                documentId,
                "+573005698325",
                birthDate,
                email,
                "Admin123*"
        );
    }
}

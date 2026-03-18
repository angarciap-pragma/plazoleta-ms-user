package com.plazoleta.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.user.application.query.GetUserByIdQuery;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetUserByIdUseCaseTest {

    private final UserPersistencePort userPersistencePort = Mockito.mock(UserPersistencePort.class);
    private final GetUserByIdUseCase getUserByIdUseCase = new GetUserByIdUseCase(userPersistencePort);

    @Test
    @DisplayName("should return user details by id")
    void shouldReturnUserDetailsById() {
        when(userPersistencePort.findById(1L)).thenReturn(Optional.of(User.builder()
                .id(1L)
                .firstName("Andrea")
                .lastName("Garcia")
                .documentId("123456789")
                .birthDate(LocalDate.parse("1990-01-01"))
                .phoneNumber("+573005698325")
                .email("owner@plazoleta.com")
                .password("encoded")
                .role(UserRole.OWNER.name())
                .active(true)
                .build()));

        assertThat(getUserByIdUseCase.getUserById(new GetUserByIdQuery(1L)).role()).isEqualTo("OWNER");
    }

    @Test
    @DisplayName("should fail when user does not exist")
    void shouldFailWhenUserDoesNotExist() {
        when(userPersistencePort.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserByIdUseCase.getUserById(new GetUserByIdQuery(99L)))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}

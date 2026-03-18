package com.plazoleta.user.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.plazoleta.common.exception.ResourceNotFoundException;
import com.plazoleta.user.application.query.GetUserAuthenticationByEmailQuery;
import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.model.UserRole;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetUserAuthenticationByEmailUseCaseTest {

    private final UserPersistencePort userPersistencePort = Mockito.mock(UserPersistencePort.class);
    private final GetUserAuthenticationByEmailUseCase useCase = new GetUserAuthenticationByEmailUseCase(userPersistencePort);

    @Test
    @DisplayName("should return user authentication data by email")
    void shouldReturnUserAuthenticationDataByEmail() {
        when(userPersistencePort.findByEmail("admin@plazoleta.com")).thenReturn(Optional.of(User.builder()
                .id(1L)
                .firstName("Platform")
                .lastName("Admin")
                .documentId("100000001")
                .birthDate(LocalDate.parse("1990-01-01"))
                .phoneNumber("+573000000001")
                .email("admin@plazoleta.com")
                .password("encoded")
                .role(UserRole.ADMIN.name())
                .active(true)
                .build()));

        assertThat(useCase.getUserAuthenticationByEmail(new GetUserAuthenticationByEmailQuery("admin@plazoleta.com")).role())
                .isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("should fail when user email does not exist")
    void shouldFailWhenUserEmailDoesNotExist() {
        when(userPersistencePort.findByEmail("missing@plazoleta.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getUserAuthenticationByEmail(
                new GetUserAuthenticationByEmailQuery("missing@plazoleta.com")
        )).isInstanceOf(ResourceNotFoundException.class);
    }
}

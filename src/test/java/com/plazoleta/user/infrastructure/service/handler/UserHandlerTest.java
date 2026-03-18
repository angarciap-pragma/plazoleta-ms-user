package com.plazoleta.user.infrastructure.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.query.GetUserAuthenticationByEmailQuery;
import com.plazoleta.user.application.query.GetUserByIdQuery;
import com.plazoleta.user.application.response.UserAuthenticationResponse;
import com.plazoleta.user.application.response.UserDetailsResponse;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
import com.plazoleta.user.domain.api.GetUserAuthenticationByEmailServicePort;
import com.plazoleta.user.domain.api.GetUserByIdServicePort;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserHandlerTest {

    private final CreateOwnerServicePort createOwnerServicePort = Mockito.mock(CreateOwnerServicePort.class);
    private final GetUserByIdServicePort getUserByIdServicePort = Mockito.mock(GetUserByIdServicePort.class);
    private final GetUserAuthenticationByEmailServicePort getUserAuthenticationByEmailServicePort =
            Mockito.mock(GetUserAuthenticationByEmailServicePort.class);
    private final UserHandler userHandler = new UserHandler(
            createOwnerServicePort,
            getUserByIdServicePort,
            getUserAuthenticationByEmailServicePort
    );

    @Test
    @DisplayName("should delegate owner creation to service port")
    void shouldDelegateOwnerCreationToServicePort() {
        CreateOwnerCommand command = new CreateOwnerCommand(
                "Andrea", "Garcia", "123456789", "+573005698325",
                LocalDate.parse("1990-01-01"), "owner@plazoleta.com", "Admin123*"
        );
        UserCreatedResponse response = UserCreatedResponse.builder().id(1L).email("owner@plazoleta.com").role("OWNER").build();
        when(createOwnerServicePort.createOwner(command)).thenReturn(response);

        assertThat(userHandler.createOwner(command).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should delegate user lookup to service port")
    void shouldDelegateUserLookupToServicePort() {
        UserDetailsResponse response = UserDetailsResponse.builder().id(1L).role("OWNER").build();
        when(getUserByIdServicePort.getUserById(new GetUserByIdQuery(1L))).thenReturn(response);

        assertThat(userHandler.getUserById(1L).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should delegate authentication lookup to service port")
    void shouldDelegateAuthenticationLookupToServicePort() {
        UserAuthenticationResponse response = UserAuthenticationResponse.builder()
                .id(1L)
                .email("owner@plazoleta.com")
                .role("OWNER")
                .active(true)
                .build();
        when(getUserAuthenticationByEmailServicePort.getUserAuthenticationByEmail(
                new GetUserAuthenticationByEmailQuery("owner@plazoleta.com")
        )).thenReturn(response);

        assertThat(userHandler.getUserAuthenticationByEmail("owner@plazoleta.com").id()).isEqualTo(1L);
    }
}

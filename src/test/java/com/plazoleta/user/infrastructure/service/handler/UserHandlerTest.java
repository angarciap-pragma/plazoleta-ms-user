package com.plazoleta.user.infrastructure.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserHandlerTest {

    private final CreateOwnerServicePort createOwnerServicePort = Mockito.mock(CreateOwnerServicePort.class);
    private final UserHandler userHandler = new UserHandler(createOwnerServicePort);

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
}

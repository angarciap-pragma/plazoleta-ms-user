package com.plazoleta.user.infrastructure.service.handler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.command.CreateEmployeeCommand;
import com.plazoleta.user.application.command.CreateCustomerCommand;
import com.plazoleta.user.application.query.GetUserAuthenticationByEmailQuery;
import com.plazoleta.user.application.query.GetUserByIdQuery;
import com.plazoleta.user.application.response.UserAuthenticationResponse;
import com.plazoleta.user.application.response.UserDetailsResponse;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateCustomerServicePort;
import com.plazoleta.user.domain.api.CreateEmployeeServicePort;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
import com.plazoleta.user.domain.api.GetUserAuthenticationByEmailServicePort;
import com.plazoleta.user.domain.api.GetUserByIdServicePort;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserHandlerTest {

    private final CreateOwnerServicePort createOwnerServicePort = Mockito.mock(CreateOwnerServicePort.class);
    private final CreateEmployeeServicePort createEmployeeServicePort = Mockito.mock(CreateEmployeeServicePort.class);
    private final CreateCustomerServicePort createCustomerServicePort = Mockito.mock(CreateCustomerServicePort.class);
    private final GetUserByIdServicePort getUserByIdServicePort = Mockito.mock(GetUserByIdServicePort.class);
    private final GetUserAuthenticationByEmailServicePort getUserAuthenticationByEmailServicePort =
            Mockito.mock(GetUserAuthenticationByEmailServicePort.class);
    private final UserHandler userHandler = new UserHandler(
            createOwnerServicePort,
            createEmployeeServicePort,
            createCustomerServicePort,
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

    @Test
    @DisplayName("should delegate employee creation to service port")
    void shouldDelegateEmployeeCreationToServicePort() {
        CreateEmployeeCommand command = new CreateEmployeeCommand(
                "John", "Doe", "123456790", "+573005698326",
                "employee@plazoleta.com", 3L, "Admin123*", 1L, 2L
        );
        UserCreatedResponse response = UserCreatedResponse.builder().id(2L).email("employee@plazoleta.com").role("EMPLOYEE").build();
        when(createEmployeeServicePort.createEmployee(command)).thenReturn(response);

        assertThat(userHandler.createEmployee(command).id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("should delegate customer creation to service port")
    void shouldDelegateCustomerCreationToServicePort() {
        CreateCustomerCommand command = new CreateCustomerCommand(
                "Jane", "Doe", "123456791", "+573005698327",
                "customer@plazoleta.com", 4L, "Admin123*"
        );
        UserCreatedResponse response = UserCreatedResponse.builder().id(3L).email("customer@plazoleta.com").role("CUSTOMER").build();
        when(createCustomerServicePort.createCustomer(command)).thenReturn(response);

        assertThat(userHandler.createCustomer(command).id()).isEqualTo(3L);
    }
}

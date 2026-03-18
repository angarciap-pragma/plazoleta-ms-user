package com.plazoleta.user.infrastructure.service.handler;

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
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Orquesta las solicitudes HTTP del módulo de usuarios.
 */
@Component
@RequiredArgsConstructor
public class UserHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

    private final CreateOwnerServicePort createOwnerServicePort;
    private final CreateEmployeeServicePort createEmployeeServicePort;
    private final CreateCustomerServicePort createCustomerServicePort;
    private final GetUserByIdServicePort getUserByIdServicePort;
    private final GetUserAuthenticationByEmailServicePort getUserAuthenticationByEmailServicePort;

    public UserCreatedResponse createOwner(final CreateOwnerCommand command) {
        LOGGER.info("Creating owner user with email {}", command.email());
        return createOwnerServicePort.createOwner(command);
    }

    public UserCreatedResponse createEmployee(final CreateEmployeeCommand command) {
        LOGGER.info("Creating employee user with email {}", command.email());
        return createEmployeeServicePort.createEmployee(command);
    }

    public UserCreatedResponse createCustomer(final CreateCustomerCommand command) {
        LOGGER.info("Creating customer user with email {}", command.email());
        return createCustomerServicePort.createCustomer(command);
    }

    public UserDetailsResponse getUserById(final Long id) {
        LOGGER.info("Fetching user with id {}", id);
        return getUserByIdServicePort.getUserById(new GetUserByIdQuery(id));
    }

    public UserAuthenticationResponse getUserAuthenticationByEmail(final String email) {
        LOGGER.info("Fetching user for authentication with email {}", email);
        return getUserAuthenticationByEmailServicePort.getUserAuthenticationByEmail(
                new GetUserAuthenticationByEmailQuery(email)
        );
    }
}

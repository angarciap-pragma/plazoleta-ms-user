package com.plazoleta.user.infrastructure.service.handler;

import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
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

    public UserCreatedResponse createOwner(final CreateOwnerCommand command) {
        LOGGER.info("Creating owner user with email {}", command.email());
        return createOwnerServicePort.createOwner(command);
    }
}

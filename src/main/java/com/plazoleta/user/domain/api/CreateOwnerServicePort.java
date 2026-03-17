package com.plazoleta.user.domain.api;

import com.plazoleta.user.application.command.CreateOwnerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;

/**
 * Define la operación de creación de propietarios.
 */
public interface CreateOwnerServicePort {

    UserCreatedResponse createOwner(CreateOwnerCommand command);
}

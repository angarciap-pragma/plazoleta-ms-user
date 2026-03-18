package com.plazoleta.user.domain.api;

import com.plazoleta.user.application.command.CreateCustomerCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;

/**
 * Define la operación de creación de clientes.
 */
public interface CreateCustomerServicePort {

    UserCreatedResponse createCustomer(CreateCustomerCommand command);
}

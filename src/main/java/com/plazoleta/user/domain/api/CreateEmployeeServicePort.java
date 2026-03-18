package com.plazoleta.user.domain.api;

import com.plazoleta.user.application.command.CreateEmployeeCommand;
import com.plazoleta.user.application.response.UserCreatedResponse;

/**
 * Define la operación de creación de empleados.
 */
public interface CreateEmployeeServicePort {

    UserCreatedResponse createEmployee(CreateEmployeeCommand command);
}

package com.plazoleta.user.domain.api;

import com.plazoleta.user.application.query.GetUserByIdQuery;
import com.plazoleta.user.application.response.UserDetailsResponse;

/**
 * Define la consulta de usuarios por identificador.
 */
public interface GetUserByIdServicePort {

    UserDetailsResponse getUserById(GetUserByIdQuery query);
}

package com.plazoleta.user.domain.api;

import com.plazoleta.user.application.query.GetUserAuthenticationByEmailQuery;
import com.plazoleta.user.application.response.UserAuthenticationResponse;

/**
 * Define la consulta interna de usuario por correo para autenticación.
 */
public interface GetUserAuthenticationByEmailServicePort {

    UserAuthenticationResponse getUserAuthenticationByEmail(GetUserAuthenticationByEmailQuery query);
}

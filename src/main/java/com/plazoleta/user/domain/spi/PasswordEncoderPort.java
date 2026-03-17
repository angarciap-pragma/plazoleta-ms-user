package com.plazoleta.user.domain.spi;

/**
 * Define la encriptación de contraseñas para usuarios.
 */
public interface PasswordEncoderPort {

    String encode(String rawPassword);
}

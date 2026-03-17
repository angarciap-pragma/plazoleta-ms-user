package com.plazoleta.user.infrastructure.security;

import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Adapta el codificador de contraseñas de Spring al dominio.
 */
@Component
@RequiredArgsConstructor
public class PasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encode(final String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}

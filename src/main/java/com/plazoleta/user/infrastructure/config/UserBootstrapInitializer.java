package com.plazoleta.user.infrastructure.config;

import com.plazoleta.user.domain.model.User;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializa el usuario administrador base requerido para autenticación.
 */
@Component
@RequiredArgsConstructor
public class UserBootstrapInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserBootstrapInitializer.class);

    private final UserBootstrapProperties userBootstrapProperties;
    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public void run(final ApplicationArguments args) {
        if (!userPersistencePort.existsByEmail(userBootstrapProperties.getEmail())) {
            userPersistencePort.save(User.createAdmin(
                    userBootstrapProperties.getFirstName(),
                    userBootstrapProperties.getLastName(),
                    userBootstrapProperties.getDocumentId(),
                    userBootstrapProperties.getPhoneNumber(),
                    userBootstrapProperties.getBirthDate(),
                    userBootstrapProperties.getEmail(),
                    passwordEncoderPort.encode(userBootstrapProperties.getPassword())
            ));
            LOGGER.info("Bootstrap admin user created with email {}", userBootstrapProperties.getEmail());
        }
    }
}

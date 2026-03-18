package com.plazoleta.user.infrastructure.config;

import com.plazoleta.common.exception.GlobalExceptionHandler;
import com.plazoleta.common.logging.TraceIdFilter;
import com.plazoleta.user.application.usecase.GetUserAuthenticationByEmailUseCase;
import com.plazoleta.user.application.usecase.CreateOwnerUseCase;
import com.plazoleta.user.application.usecase.GetUserByIdUseCase;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
import com.plazoleta.user.domain.api.GetUserAuthenticationByEmailServicePort;
import com.plazoleta.user.domain.api.GetUserByIdServicePort;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Registra los beans de aplicación del microservicio de usuarios.
 */
@Configuration
@Import(GlobalExceptionHandler.class)
@EnableConfigurationProperties(UserBootstrapProperties.class)
public class UseCaseConfig {

    @Bean
    public CreateOwnerServicePort createOwnerServicePort(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateOwnerUseCase(userPersistencePort, passwordEncoderPort);
    }

    @Bean
    public GetUserByIdServicePort getUserByIdServicePort(final UserPersistencePort userPersistencePort) {
        return new GetUserByIdUseCase(userPersistencePort);
    }

    @Bean
    public GetUserAuthenticationByEmailServicePort getUserAuthenticationByEmailServicePort(
            final UserPersistencePort userPersistencePort
    ) {
        return new GetUserAuthenticationByEmailUseCase(userPersistencePort);
    }

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }
}

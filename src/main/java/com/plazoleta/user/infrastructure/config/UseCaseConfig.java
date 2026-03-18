package com.plazoleta.user.infrastructure.config;

import com.plazoleta.common.exception.GlobalExceptionHandler;
import com.plazoleta.common.logging.TraceIdFilter;
import com.plazoleta.user.application.usecase.GetUserAuthenticationByEmailUseCase;
import com.plazoleta.user.application.usecase.CreateCustomerUseCase;
import com.plazoleta.user.application.usecase.CreateEmployeeUseCase;
import com.plazoleta.user.application.usecase.CreateOwnerUseCase;
import com.plazoleta.user.application.usecase.GetUserByIdUseCase;
import com.plazoleta.user.domain.api.CreateCustomerServicePort;
import com.plazoleta.user.domain.api.CreateEmployeeServicePort;
import com.plazoleta.user.domain.api.CreateOwnerServicePort;
import com.plazoleta.user.domain.api.GetUserAuthenticationByEmailServicePort;
import com.plazoleta.user.domain.api.GetUserByIdServicePort;
import com.plazoleta.user.domain.spi.PasswordEncoderPort;
import com.plazoleta.user.domain.spi.RestaurantOwnershipQueryPort;
import com.plazoleta.user.domain.spi.UserPersistencePort;
import com.plazoleta.user.infrastructure.drivenadapters.rest.properties.RestaurantServiceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestClient;

/**
 * Registra los beans de aplicación del microservicio de usuarios.
 */
@Configuration
@Import(GlobalExceptionHandler.class)
@EnableConfigurationProperties({
        UserBootstrapProperties.class,
        RestaurantServiceProperties.class
})
public class UseCaseConfig {

    @Bean
    public CreateOwnerServicePort createOwnerServicePort(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateOwnerUseCase(userPersistencePort, passwordEncoderPort);
    }

    @Bean
    public CreateEmployeeServicePort createEmployeeServicePort(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort,
            final RestaurantOwnershipQueryPort restaurantOwnershipQueryPort
    ) {
        return new CreateEmployeeUseCase(userPersistencePort, passwordEncoderPort, restaurantOwnershipQueryPort);
    }

    @Bean
    public CreateCustomerServicePort createCustomerServicePort(
            final UserPersistencePort userPersistencePort,
            final PasswordEncoderPort passwordEncoderPort
    ) {
        return new CreateCustomerUseCase(userPersistencePort, passwordEncoderPort);
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

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}

package com.plazoleta.user.infrastructure.config;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Representa la configuración del usuario administrador inicial.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "user.bootstrap.admin")
public class UserBootstrapProperties {

    private Long id;
    private String firstName;
    private String lastName;
    private String documentId;
    private String phoneNumber;
    private LocalDate birthDate;
    private String email;
    private String password;
}

package com.plazoleta.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada del microservicio de usuarios.
 */
@SpringBootApplication
public class MsUserApplication {

    public static void main(final String[] args) {
        SpringApplication.run(MsUserApplication.class, args);
    }
}

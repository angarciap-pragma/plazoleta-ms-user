package com.plazoleta.user.infrastructure.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class PasswordEncoderAdapterTest {

    @Test
    @DisplayName("should encode password using delegated encoder")
    void shouldEncodePasswordUsingDelegatedEncoder() {
        PasswordEncoderAdapter adapter = new PasswordEncoderAdapter(new BCryptPasswordEncoder());

        String encoded = adapter.encode("Admin123*");

        assertThat(encoded).isNotBlank();
        assertThat(encoded).startsWith("$2");
    }
}

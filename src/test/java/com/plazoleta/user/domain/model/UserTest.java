package com.plazoleta.user.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.plazoleta.common.exception.BadRequestException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("should create owner with owner role")
    void shouldCreateOwnerWithOwnerRole() {
        User user = User.createOwner(
                "Andrea",
                "Garcia",
                "123456789",
                "+573005698325",
                LocalDate.now().minusYears(20),
                "andrea@plazoleta.com",
                "encoded"
        );

        assertThat(user.getRole()).isEqualTo(UserRole.OWNER.name());
        assertThat(user.getEmail()).isEqualTo("andrea@plazoleta.com");
        assertThat(user.getPassword()).isEqualTo("encoded");
    }

    @Test
    @DisplayName("should reject underage owner")
    void shouldRejectUnderageOwner() {
        assertThatThrownBy(() -> User.createOwner(
                "Andrea",
                "Garcia",
                "123456789",
                "+573005698325",
                LocalDate.now().minusYears(17),
                "andrea@plazoleta.com",
                "encoded"
        )).isInstanceOf(BadRequestException.class);
    }
}

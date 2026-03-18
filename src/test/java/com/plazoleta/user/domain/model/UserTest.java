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

    @Test
    @DisplayName("should create admin user")
    void shouldCreateAdminUser() {
        User user = User.createAdmin(
                "Admin",
                "Plazoleta",
                "987654321",
                "+573005698326",
                LocalDate.now().minusYears(25),
                "admin@plazoleta.com",
                "encoded"
        );

        assertThat(user.getRole()).isEqualTo(UserRole.ADMIN.name());
        assertThat(user.getBirthDate()).isNotNull();
        assertThat(user.getRestaurantId()).isNull();
    }

    @Test
    @DisplayName("should create employee user")
    void shouldCreateEmployeeUser() {
        User user = User.createEmployee(
                "Employee",
                "User",
                "555666777",
                "+573005698327",
                "employee@plazoleta.com",
                "encoded",
                1L
        );

        assertThat(user.getRole()).isEqualTo(UserRole.EMPLOYEE.name());
        assertThat(user.getRestaurantId()).isEqualTo(1L);
        assertThat(user.getBirthDate()).isNull();
    }

    @Test
    @DisplayName("should create customer user")
    void shouldCreateCustomerUser() {
        User user = User.createCustomer(
                "Customer",
                "User",
                "888999000",
                "+573005698328",
                "customer@plazoleta.com",
                "encoded"
        );

        assertThat(user.getRole()).isEqualTo(UserRole.CUSTOMER.name());
        assertThat(user.getRestaurantId()).isNull();
        assertThat(user.getBirthDate()).isNull();
    }

    @Test
    @DisplayName("should resolve user role from id")
    void shouldResolveUserRoleFromId() {
        assertThat(UserRole.fromId(1L)).isEqualTo(UserRole.ADMIN);
        assertThat(UserRole.fromId(2L)).isEqualTo(UserRole.OWNER);
        assertThat(UserRole.fromId(3L)).isEqualTo(UserRole.EMPLOYEE);
        assertThat(UserRole.fromId(4L)).isEqualTo(UserRole.CUSTOMER);
    }

    @Test
    @DisplayName("should reject unknown role id")
    void shouldRejectUnknownRoleId() {
        assertThatThrownBy(() -> UserRole.fromId(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown role id");
    }
}

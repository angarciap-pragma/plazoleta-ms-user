package com.plazoleta.user.domain.model;

/**
 * Define los roles manejados por el microservicio de usuarios.
 */
public enum UserRole {
    ADMIN(1L),
    OWNER(2L),
    EMPLOYEE(3L),
    CUSTOMER(4L);

    private final Long id;

    UserRole(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public static UserRole fromId(final Long id) {
        for (UserRole role : values()) {
            if (role.id.equals(id)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role id");
    }
}

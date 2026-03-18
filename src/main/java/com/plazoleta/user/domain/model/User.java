package com.plazoleta.user.domain.model;

import com.plazoleta.user.domain.exception.UserErrorCode;
import com.plazoleta.common.exception.BadRequestException;
import java.time.LocalDate;
import java.time.Period;
import lombok.Builder;
import lombok.Getter;

/**
 * Representa el agregado de usuario del microservicio.
 */
@Getter
@Builder
public class User {

    private static final int LEGAL_ADULT_AGE = 18;

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String documentId;
    private final String phoneNumber;
    private final LocalDate birthDate;
    private final String email;
    private final String password;
    private final String role;
    private final boolean active;
    private final Long restaurantId;

    public static User createOwner(
            final String firstName,
            final String lastName,
            final String documentId,
            final String phoneNumber,
            final LocalDate birthDate,
            final String email,
            final String encodedPassword
    ) {
        validateLegalAge(birthDate);
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .documentId(documentId)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .email(email)
                .password(encodedPassword)
                .role(UserRole.OWNER.name())
                .active(true)
                .restaurantId(null)
                .build();
    }

    public static User createAdmin(
            final String firstName,
            final String lastName,
            final String documentId,
            final String phoneNumber,
            final LocalDate birthDate,
            final String email,
            final String encodedPassword
    ) {
        validateLegalAge(birthDate);
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .documentId(documentId)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .email(email)
                .password(encodedPassword)
                .role(UserRole.ADMIN.name())
                .active(true)
                .restaurantId(null)
                .build();
    }

    public static User createEmployee(
            final String firstName,
            final String lastName,
            final String documentId,
            final String phoneNumber,
            final String email,
            final String encodedPassword,
            final Long restaurantId
    ) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .documentId(documentId)
                .phoneNumber(phoneNumber)
                .birthDate(null)
                .email(email)
                .password(encodedPassword)
                .role(UserRole.EMPLOYEE.name())
                .active(true)
                .restaurantId(restaurantId)
                .build();
    }

    public static User createCustomer(
            final String firstName,
            final String lastName,
            final String documentId,
            final String phoneNumber,
            final String email,
            final String encodedPassword
    ) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .documentId(documentId)
                .phoneNumber(phoneNumber)
                .birthDate(null)
                .email(email)
                .password(encodedPassword)
                .role(UserRole.CUSTOMER.name())
                .active(true)
                .restaurantId(null)
                .build();
    }

    private static void validateLegalAge(final LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < LEGAL_ADULT_AGE) {
            throw new BadRequestException(UserErrorCode.UNDERAGE_OWNER);
        }
    }
}

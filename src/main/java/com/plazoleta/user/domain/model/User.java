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
                .build();
    }

    private static void validateLegalAge(final LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < LEGAL_ADULT_AGE) {
            throw new BadRequestException(UserErrorCode.UNDERAGE_OWNER);
        }
    }
}

package com.plazoleta.user.domain.exception;

import com.plazoleta.common.error.ErrorCode;
import org.springframework.http.HttpStatus;

/**
 * Define los códigos de error propios del microservicio de usuarios.
 */
public enum UserErrorCode implements ErrorCode {
    EMAIL_ALREADY_EXISTS("USER_409_EMAIL_ALREADY_EXISTS", "Email already exists", HttpStatus.CONFLICT),
    DOCUMENT_ALREADY_EXISTS("USER_409_DOCUMENT_ALREADY_EXISTS", "Document already exists", HttpStatus.CONFLICT),
    UNDERAGE_OWNER("USER_400_UNDERAGE_OWNER", "Owner must be of legal age", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND("USER_404_USER_NOT_FOUND", "User not found", HttpStatus.NOT_FOUND);

    private final String code;
    private final String defaultMessage;
    private final HttpStatus status;

    UserErrorCode(final String code, final String defaultMessage, final HttpStatus status) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.status = status;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultMessage() {
        return defaultMessage;
    }

    @Override
    public HttpStatus status() {
        return status;
    }
}

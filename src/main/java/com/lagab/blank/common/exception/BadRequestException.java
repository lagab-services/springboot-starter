package com.lagab.blank.common.exception;

import java.io.Serial;

public class BadRequestException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("Bad Request!");
    }

    public BadRequestException(final String message) {
        super(message);
    }
}

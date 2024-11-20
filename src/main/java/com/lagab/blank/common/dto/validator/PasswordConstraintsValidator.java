package com.lagab.blank.common.dto.validator;

import com.lagab.blank.common.dto.annotation.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public final class PasswordConstraintsValidator implements ConstraintValidator<Password, String> {
    private static final int MIN_LENGTH = 6;

    private static final int MAX_LENGTH = 32;

    private boolean detailedMessage;

    @Override
    public void initialize(final Password constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        detailedMessage = constraintAnnotation.detailedMessage();
    }

    @Override
    public boolean isValid(final String password, final ConstraintValidatorContext context) {
        if (password == null) {
            return false; // Null passwords are invalid
        }

        // Define password criteria: at least 8 chars, 1 uppercase, 1 lowercase, 1 digit, 1 special char
        String passwordPattern =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(passwordPattern);
    }
}
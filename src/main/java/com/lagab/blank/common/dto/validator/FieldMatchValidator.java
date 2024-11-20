package com.lagab.blank.common.dto.validator;

import java.lang.reflect.Field;

import com.lagab.blank.common.dto.annotation.FieldMatch;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstField;

    private String secondField;

    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstField = constraintAnnotation.first();
        secondField = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        boolean valid = true;

        try {
            final Object firstProperty = getFieldValue(obj, firstField);
            final Object secondProperty = getFieldValue(obj, secondField);

            valid = (firstProperty == null && secondProperty == null)
                    || (firstProperty != null && firstProperty.equals(secondProperty));
        } catch (final Exception e) {
            log.warn("Error while validating fields {} - {}", this.getClass().getName(), e.getMessage());
        }

        if (!valid) {
            context.buildConstraintViolationWithTemplate(message)
                   .addPropertyNode(firstField)
                   .addConstraintViolation()
                   .disableDefaultConstraintViolation();
        }

        return valid;
    }

    private Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName); // Get the field by name
        field.setAccessible(true); // Make it accessible if it's private
        return field.get(obj); // Retrieve the value
    }
}

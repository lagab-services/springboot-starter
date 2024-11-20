package com.lagab.blank.common.dto.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.lagab.blank.common.dto.validator.MinListSizeValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = MinListSizeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinListSize {
    String message() default "The list must be a minimum size";

    long min() default -1L;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
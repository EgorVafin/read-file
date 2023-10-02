package org.example.util.validation.unique2;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DocumentUniqueUrlValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DocumentUniqueUrlConstraint {
    String message() default "URL должен быть уникальным";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

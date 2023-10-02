package org.example.util.validation.unique;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Repeatable(FieldUniques.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldUniqueValidator.class)
public @interface FieldUnique {
    String message() default "Значение не уникально";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String field();
    Class<? extends UniqueFieldValidator> service();
}

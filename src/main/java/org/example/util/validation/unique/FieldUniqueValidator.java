package org.example.util.validation.unique;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("DocumentFormRequestUniqueValidator")
@RequiredArgsConstructor
public class FieldUniqueValidator implements ConstraintValidator<FieldUnique, Object> {

    private ApplicationContext context;
    private String fieldName;
    private Class<? extends UniqueFieldValidator> fieldValidator;
    private String message;


    @Override
    public void initialize(FieldUnique constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.fieldName = constraintAnnotation.field();
        this.fieldValidator = constraintAnnotation.service();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            UniqueFieldValidator fieldValidatorService = context.getBean(this.fieldValidator);

            boolean isValid = fieldValidatorService.exist(value, this.fieldName);

            if (!isValid) {
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(fieldName).addConstraintViolation();
            }

            return isValid;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package org.example.util.validation.unique;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("DocumentFormRequestUniqueValidator")
@RequiredArgsConstructor
public class FieldUniqueValidator implements ConstraintValidator<FieldUnique, Object> {
    @Autowired
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
            //получение бина сервиса поиска сущ записей
            UniqueFieldValidator fieldValidatorService = context.getBean(this.fieldValidator);

            //поиск сущ ли такая запись со значением поля
            boolean isValid = fieldValidatorService.exist(value, this.fieldName);

            if (!isValid) {
                //отключаем стандартное поведение (добавление ошибки не на поле, а на сам класс)
                constraintValidatorContext.disableDefaultConstraintViolation();
                //формируем ошибку для нужного поля
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate(message)
                        .addPropertyNode(fieldName)
                        .addConstraintViolation();
            }

            return isValid;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package org.example.util.validation.unique2;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.controller.DocumentEntityRepository;
import org.springframework.stereotype.Service;

@Service //нужно ли ?
@RequiredArgsConstructor
public class DocumentUniqueUrlValidator implements ConstraintValidator<DocumentUniqueUrlConstraint, String> {
    private final DocumentEntityRepository repository;

    @Override
    public void initialize(DocumentUniqueUrlConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return !repository.findByUrl(value).isPresent();
    }
}

package org.example.util.validation.unique1;

import lombok.RequiredArgsConstructor;
import org.example.controller.DocumentCreateDto;
import org.example.controller.DocumentEntityRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class DocumentUniqueNameValidator implements Validator {

    private final DocumentEntityRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return DocumentCreateDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DocumentCreateDto documentCreateDto = (DocumentCreateDto) target;

        if (repository.findByName(documentCreateDto.getName()).isPresent()) {
            errors.rejectValue("name", "", "Это имя уже используется");
        }
    }
}

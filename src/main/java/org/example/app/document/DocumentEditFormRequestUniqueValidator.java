package org.example.app.document;

import lombok.RequiredArgsConstructor;
import org.example.app.document.edit.DocumentEditFormRequest;
import org.example.util.validation.unique.UniqueFieldValidator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentEditFormRequestUniqueValidator implements UniqueFieldValidator {

    private final DocumentEntityRepository documentEntityRepository;

    @Override
    public boolean exist(Object validated, String fieldName) {

        if (!(validated instanceof DocumentEditFormRequest)) {
            throw new RuntimeException("Invalid object class");
        }

        DocumentEditFormRequest dto = (DocumentEditFormRequest) validated;

        return switch (fieldName) {
            case "name" -> validateName(dto);
            default -> throw new RuntimeException("Invalid validation field: " + fieldName);
        };
    }

    private boolean validateName(DocumentEditFormRequest dto) {

        if (dto.getId() != null ) {

            return documentEntityRepository.findByNameAndIdNot(dto.getName(), dto.getId())
                    .isEmpty();

        }

        return documentEntityRepository.findByName(dto.getName()).isEmpty();
    }
}

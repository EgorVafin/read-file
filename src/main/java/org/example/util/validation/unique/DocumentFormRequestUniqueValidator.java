package org.example.util.validation.unique;

import lombok.RequiredArgsConstructor;
import org.example.app.document.DocumentCreateDto;
import org.example.app.document.DocumentEntityRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentFormRequestUniqueValidator implements UniqueFieldValidator {

    private final DocumentEntityRepository repository;

    @Override
    public boolean exist(Object validated, String fieldName) {
        if (!(validated instanceof DocumentCreateDto)) {
            throw new RuntimeException("Invalid obj class");
        }

        DocumentCreateDto dto = (DocumentCreateDto) validated;
        return switch (fieldName) {
            case "name" -> validateName(dto);
            case "url" -> validateUrl(dto);
            default -> throw new RuntimeException("Invalid validation field: " + fieldName);
        };
    }

    private boolean validateName(DocumentCreateDto dto) {
        return repository.findByName(dto.getName())
                .isEmpty();
    }

    private boolean validateUrl(DocumentCreateDto dto) {
        return repository.findByUrl(dto.getUrl())
                .isEmpty();
    }
}

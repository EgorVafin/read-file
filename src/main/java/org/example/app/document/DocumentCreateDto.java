package org.example.app.document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.util.validation.unique.DocumentFormRequestUniqueValidator;
import org.example.util.validation.unique.FieldUnique;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@FieldUnique(field = "name", service = DocumentFormRequestUniqueValidator.class, message = "Название должно быть уникально")
@FieldUnique(field = "url", service = DocumentFormRequestUniqueValidator.class, message = "URL должно быть уникально")
public class DocumentCreateDto {

    private long id;

    @NotBlank
    @Size(max = 256)
    private String name;

    @NotBlank
    @Size(max = 512)
    @URL(message = "Должно содержать допустимый URL")
    private String url;
}

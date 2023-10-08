package org.example.app.document.edit;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.app.document.DocumentEditFormRequestUniqueValidator;
import org.example.util.validation.unique.FieldUnique;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@Getter
@Setter
@FieldUnique(field = "name", service = DocumentEditFormRequestUniqueValidator.class, message = "Такое имя уже используется")
public class DocumentEditFormRequest {

    private Long id;

    @NotBlank(message = "Не должно быть пустым")
    @Length(max = 128)
    private String name;
}

package org.example.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.util.validation.unique2.DocumentUniqueUrlConstraint;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor

//@FieldsValueMatch.List({
//        @FieldsValueMatch(
//                field = "url",
//                fieldMatch = "name",
//                message = "Passwords do not match!"
//        ),
//        @FieldsValueMatch(
//                field = "url",
//                fieldMatch = "name",
//                message = "Email addresses do not match!"
//        )
//})

//@FieldUnique(field = "name", service = DocumentFormRequestUniqueValidator.class, message = "Название должно быть уникально")
public class DocumentCreateDto {

    private long id;

    @NotBlank
    @Size(max = 256)
    //@ContactNumberConstraint
    private String name;

    @NotBlank
    @Size(max = 512)
    @URL(message = "Должно содержать допустимый URL")
    @DocumentUniqueUrlConstraint
    private String url;
}

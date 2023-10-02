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
public class WordStatListDto {

    private String word;

    private int count;
}

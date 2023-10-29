package org.example.app.word;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.DocumentEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WordStatFilter {
    private String filter_word;
    private Integer filter_frequency;
    private List<DocumentEntity> filter_document = new ArrayList<>();
}

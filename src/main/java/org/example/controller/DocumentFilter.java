package org.example.controller;

import lombok.Getter;
import lombok.Setter;
import org.example.entity.DocumentEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentFilter {
    private String filter_name;
    private List<DocumentEntity> filter_documents = new ArrayList<>();
}

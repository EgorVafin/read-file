package org.example.app.document.edit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.app.document.DocumentEntityRepository;
import org.example.entity.DocumentEntity;
import org.example.lib.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.print.Doc;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DocumentEditController {
    private final DocumentEntityRepository documentEntityRepository;

    @GetMapping("/document/{id}/edit")
    public String edit(@PathVariable("id") long id,
                       Model model) {
        DocumentEntity document = documentEntityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объект не найден"));

        model.addAttribute("document", document);
        return "document/edit_form";
    }

    @PostMapping("/document/{id}/edit")
    public String update(@PathVariable("id") long id,
                         @Valid @ModelAttribute(name = "document") DocumentEditFormRequest documentEditFormRequest,
                         BindingResult bindingResult,
                         Model model) {

        DocumentEntity document = documentEntityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Объект не найден"));
        model.addAttribute("document", documentEditFormRequest);
        if(bindingResult.hasErrors()) {
            return "document/edit_form";
        }

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(documentEditFormRequest, document);
        documentEntityRepository.save(document);

        return "redirect:/document";

    }
}

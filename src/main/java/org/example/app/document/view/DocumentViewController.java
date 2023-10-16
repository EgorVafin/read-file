package org.example.app.document.view;

import lombok.RequiredArgsConstructor;
import org.example.app.document.DocumentEntityRepository;
import org.example.app.document.DocumentFilter;
import org.example.app.word.WordStatEntityRepository;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DocumentViewController {

    private final DocumentEntityRepository documentEntityRepository;
    private final WordStatEntityRepository wordStatEntityRepository;



    @GetMapping("/document/{id}")
    public String view(@PathVariable("id") long id,
                       @RequestParam(required = false, defaultValue = "1") Integer page,
                       @RequestParam(required = false, defaultValue = "20") Integer perPage,
                       @RequestParam(required = false, defaultValue = "word") String sortBy,
                       @RequestParam(required = false, defaultValue = "ASC") String order,
                       Model model) {

        Optional<DocumentEntity> document = documentEntityRepository.findById(id);
        model.addAttribute("document", document.get());

        Pageable paging = order.equals("ASC") ?
                PageRequest.of(page - 1, perPage, Sort.by(sortBy).ascending()) :
                PageRequest.of(page - 1, perPage, Sort.by(sortBy).descending());

        Page<WordStatEntity> wordStatEntityList = wordStatEntityRepository.findByDocument(paging, document);
        //todo правильно ли так. нужно ли два репозитория
        model.addAttribute("words", wordStatEntityList);
        return "document/view";
    }

    @GetMapping("/document")
    public String list(Model model, DocumentFilter filter) {

        List<DocumentEntity> documentEntityList = documentEntityRepository.findAll(buildSearch(filter));
        model.addAttribute("filter", filter);
        model.addAttribute("documents", documentEntityList);
        return "document/list";
    }

    private Specification<DocumentEntity> buildSearch(DocumentFilter filter) {
        Specification<DocumentEntity> searchCriteria = Specification.where(null);

        if (filter.getFilter_name() != null && !filter.getFilter_name().isBlank()) {
            searchCriteria = searchCriteria.and(DocumentEntityRepository.documentNameLike(filter.getFilter_name()));
        }

        if (filter.getFilter_url() != null && !filter.getFilter_url().isBlank()) {
            searchCriteria = searchCriteria.or(DocumentEntityRepository.documentUrlLike(filter.getFilter_url()));
        }
        return searchCriteria;
    }

}

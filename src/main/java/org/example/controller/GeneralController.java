package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.example.repository.WordStatEntityRepository;
import org.example.service.WordCommonCounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Controller
@RequiredArgsConstructor
public class GeneralController {
    private final WordStatEntityRepository wordStatEntityRepository;
    private final DocumentEntityRepository documentEntityRepository;
    private final WordCommonCounter wordCommonCounter;

    @GetMapping("/")
    public String index(@RequestParam(required = false, defaultValue = "0") Integer offset,
                        @RequestParam(required = false, defaultValue = "1") Integer limit,
                        DocumentFilter filter,
                        Model model) {

        //на какой стороне лучше сделать подсчет?
        //List<WordStatEntity> wordStatEntityList = wordStatEntityRepository.findCommonWordsStat();

        List<WordStatEntity> wordStatEntityList = wordStatEntityRepository.findAll();

        //паджинацию делать вручную?
        model.addAttribute("words", wordCommonCounter.addAll(wordStatEntityList));

        Pageable paging = PageRequest.of(offset, limit);
        Page<DocumentEntity> page = documentEntityRepository.findAll(buildSearch(filter), paging);
        model.addAttribute("documents", page);
        model.addAttribute("filter", filter);

        return "index";
    }

    private Specification<DocumentEntity> buildSearch(DocumentFilter filter) {
        Specification<DocumentEntity> searchCriteria = where(null);

        //Почему не видит методы в репозитории?

//        if (filter.getFilter_name() != null && !filter.getFilter_name().isBlank()) {
//            searchCriteria = searchCriteria.and(documentNameLike(filter.getFilter_name()));
//        }
//
//
//        if (filter.getFilter_documents() != null && !filter.getFilter_documents().isEmpty()) {
//            searchCriteria = searchCriteria.and(documentsIn(filter.getFilter_documents()));
//        }

        return searchCriteria;
    }
}

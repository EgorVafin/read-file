package org.example.app.word;

import lombok.RequiredArgsConstructor;
import org.example.app.document.DocumentEntityRepository;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.example.service.WordCommonCounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GeneralController {
    private final WordStatEntityRepository wordStatEntityRepository;
    private final DocumentEntityRepository documentEntityRepository;
    private final WordCommonCounter wordCommonCounter;

    @GetMapping("/")
    public String index(@RequestParam(required = false, defaultValue = "1") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer perPage,
                        Model model, WordStatFilter filter) {

        List<DocumentEntity> documents = documentEntityRepository.findAll();
        model.addAttribute("documents", documents);
        Pageable paging = PageRequest.of(page - 1, perPage);
        model.addAttribute("filter", filter);

        if (filter.getFilter_word() == null && filter.getFilter_frequency() == null) {
            Page<SummaryWordStat> summaryWordStatList = wordStatEntityRepository.findCommonWordsStat(paging);


            model.addAttribute("words", summaryWordStatList);
        } else {
            List<WordStatEntity> summaryWordStatList2 = wordStatEntityRepository.findAll(buildSearch(filter));

            List<String> words = new ArrayList<>();
            for (WordStatEntity entity : summaryWordStatList2) {
                words.add(entity.getWord());
            }
            Page<SummaryWordStat> summaryWordStatList3 = wordStatEntityRepository.findCommonWordsStatAfterFilter(paging, words);
            model.addAttribute("words", summaryWordStatList3);
        }



        return "index";
    }

    private Specification<WordStatEntity> buildSearch(WordStatFilter filter) {
        Specification<WordStatEntity> searchCriteria = Specification.where(null);

        if (filter.getFilter_word() != null && !filter.getFilter_word().isBlank()) {
            searchCriteria = searchCriteria.and(WordStatEntityRepository.wordNameLike(filter.getFilter_word()));
        }

        //todo делает выборку в Entity, но нужно искать в SummaryWordStat. Поэтому неправильно считает FREQUENCY
        if (filter.getFilter_frequency() != null /*&& filter.getFilter_frequency().matches("[0-9]+")*/) {
            searchCriteria = searchCriteria.or(WordStatEntityRepository.
                    wordFrequencyEqual(Integer.valueOf(filter.getFilter_frequency())));
        }

        if (filter.getFilter_document() != null && !filter.getFilter_document().isEmpty()) {
            searchCriteria = searchCriteria.and(WordStatEntityRepository.documentsIn(filter.getFilter_document()));
        }

        return searchCriteria;
    }
}

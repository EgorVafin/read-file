package org.example.app.word;

import lombok.RequiredArgsConstructor;
import org.example.app.document.DocumentEntityRepository;
import org.example.app.oauth.OAuthVKController;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
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

    @GetMapping("/")
    public String index(@RequestParam(required = false, defaultValue = "1") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer perPage,
                        WordStatFilter filter,
                        Model model) {

        List<DocumentEntity> documents = documentEntityRepository.findAll();
        model.addAttribute("documents", documents);
        Pageable paging = PageRequest.of(page - 1, perPage);
        var words = wordStatsFilters(filter, paging);
        model.addAttribute("words", words);
        model.addAttribute("vkOauthUrl", OAuthVKController.VkOauthUrl);

        return "index";
    }

    private Specification<WordStatEntity> buildSearch(WordStatFilter filter) {
        Specification<WordStatEntity> searchCriteria = Specification.where(null);

        if (filter.getFilter_word() != null && !filter.getFilter_word().isBlank()) {
            searchCriteria = searchCriteria.and(WordStatEntityRepository.wordNameLike(filter.getFilter_word()));
        }

        if (filter.getFilter_frequency() != null /*&& filter.getFilter_frequency().matches("[0-9]+")*/) {
            searchCriteria = searchCriteria.or(WordStatEntityRepository.
                    wordFrequencyEqual(Integer.valueOf(filter.getFilter_frequency())));
        }

        if (filter.getFilter_document() != null && !filter.getFilter_document().isEmpty()) {
            searchCriteria = searchCriteria.and(WordStatEntityRepository.documentsIn(filter.getFilter_document()));
        }
        return searchCriteria;
    }

    @GetMapping("/access_denied")
    public String accessDenied() {
        return "access_denied";
    }

    private Page<SummaryWordStat> wordStatsFilters(WordStatFilter filter,
                                                   Pageable paging) {

        Page<SummaryWordStat> stats = null;
        if (filter.getFilter_word() == null
                && filter.getFilter_frequency() == null
                && filter.getFilter_document().size() == 0) {
            return wordStatEntityRepository.findNoFilters(paging);
        }

        if (filter.getFilter_word() != null
                && filter.getFilter_frequency() == null
                && filter.getFilter_document().size() == 0) {
            return wordStatEntityRepository.findFilterByWord(paging, filter.getFilter_word());
        }

        if (filter.getFilter_word() == null
                && filter.getFilter_frequency() != null
                && filter.getFilter_document().size() == 0) {
            return wordStatEntityRepository.findFilterByFrequency(paging, filter.getFilter_frequency());
        }

        if (filter.getFilter_word() != null
                && filter.getFilter_frequency() != null
                && filter.getFilter_document().size() == 0) {
            return wordStatEntityRepository.findFilterByWordAndFrequency(paging,
                    filter.getFilter_word(),
                    filter.getFilter_frequency());
        }

        List<Long> documentIds = filter.getFilter_document()
                .stream()
                .map(DocumentEntity::getId).toList();
        if (
                filter.getFilter_word().isEmpty()
                        && filter.getFilter_frequency() == null
                        && !filter.getFilter_document().isEmpty()) {

            return wordStatEntityRepository.findFilterByDocuments(paging, documentIds);
        }

        return stats;
    }
}

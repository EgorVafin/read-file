package org.example.app.document;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.app.word.SummaryWordStat;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.example.normalizer.LongWordsNormalizer;
import org.example.normalizer.Normalizer;
import org.example.normalizer.NormalizerCollection;
import org.example.normalizer.ShortWordsNormalizer;
import org.example.app.word.WordStatEntityRepository;
import org.example.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final WordStatEntityRepository wordStatEntityRepository;
    private final DocumentEntityRepository documentEntityRepository;
    private final UrlDataLoader urlParser;

    @GetMapping("/document/add")
    public String create(Model model) {
        DocumentCreateDto documentCreateDto = new DocumentCreateDto();
        model.addAttribute("document", documentCreateDto);

        return "document/add";
    }

    @PostMapping("/document/add")
    public String create(
            @Valid @ModelAttribute(name = "document") DocumentCreateDto documentCreateDto,
            BindingResult bindingResult,
            Model model) {

        String text = null;

        try {
            text = urlParser.parse(documentCreateDto.getUrl());
        } catch (UrlDataLoadException e) {
            //todo проверить, добавляется ли ошибка к полю url
            //bindingResult.addError(new ObjectError("document", "Error fetch data"));

            ObjectError error = new ObjectError("document", "???");
            bindingResult.rejectValue("url", "value.negative", new ObjectError[]{error}, "Ошибка получения данных");
            model.addAttribute("document", documentCreateDto);
            return "document/add";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("document", documentCreateDto);
            return "document/add";
        }

        List<String> words = new LineToWordsSplitter().split(text);

        Normalizer normalizer = new NormalizerCollection(List.of(new ShortWordsNormalizer(3),
                new LongWordsNormalizer(15)));

        //todo как работает normalizer
        WordCounter wordCounter = new WordCounter();
        wordCounter.addAll(normalizer.normalize(words));

        //как правильно сделать mapping?
        ModelMapper modelMapper = new ModelMapper();
        DocumentEntity documentEntity = modelMapper.map(documentCreateDto, DocumentEntity.class);
        documentEntity.setText(text);
        documentEntity.setScrapedAt(new Date());

        //толстый или тонкий контроллер?

        List<WordStat> wordStatList = wordCounter.getOrderedStat();

        List<WordStatEntity> wordStatEntityList = new ArrayList<>();

        for (WordStat element : wordStatList) {
            WordStatEntity wordStatEntity = new WordStatEntity();
            wordStatEntity.setDocument(documentEntity);
            wordStatEntity.setWord(element.getWord());
            wordStatEntity.setCount(element.getCount());
            wordStatEntityList.add(wordStatEntity);
        }

        documentEntityRepository.save(documentEntity);
        wordStatEntityRepository.saveAll(wordStatEntityList);

        return "redirect:/";
    }

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

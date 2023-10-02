package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.example.normalizer.LongWordsNormalizer;
import org.example.normalizer.Normalizer;
import org.example.normalizer.NormalizerCollection;
import org.example.normalizer.ShortWordsNormalizer;
import org.example.repository.WordStatEntityRepository;
import org.example.service.LineToWordsSplitter;
import org.example.service.UrlParser;
import org.example.service.WordCounter;
import org.example.service.WordStat;
import org.example.util.validation.unique1.DocumentUniqueNameValidator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DocumentController {

    private final WordStatEntityRepository wordStatEntityRepository;
    private final DocumentEntityRepository documentEntityRepository;
    private final DocumentUniqueNameValidator documentUniqueNameValidator;
    private final WordCounter wordCounter;
    private final UrlParser urlParser;

    @GetMapping("/document/add")
    public String create(Model model) {
        DocumentCreateDto documentCreateDto = new DocumentCreateDto();
        model.addAttribute("document", documentCreateDto);

        return "document/add";
    }

    @PostMapping("/document/add")
    public String create(@Valid @ModelAttribute(name = "document") DocumentCreateDto documentCreateDto
            , BindingResult bindingResult, Model model) {

        documentUniqueNameValidator.validate(documentCreateDto, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("document", documentCreateDto);
            return "document/add";
        }

        String text = urlParser.parse(documentCreateDto.getUrl());
        if (text == null) {
            return "redirect:/";
        }

        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();
        List<String> words = lineToWordsSplitter.split(text);

        Normalizer normalizer = new NormalizerCollection(List.of(new ShortWordsNormalizer(2),
                new LongWordsNormalizer(15)));

        wordCounter.addAll(normalizer.normalize(words));

        //как правильно сделать mapping?
        ModelMapper modelMapper = new ModelMapper();
        DocumentEntity documentEntity = modelMapper.map(documentCreateDto, DocumentEntity.class);
        documentEntity.setText(text);
        documentEntity.setScrapedAt(new Date());

        //толстый или тонкий контроллер?
        List<org.example.service.WordStat> wordStatList = wordCounter.getOrderedStat();
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

        //Попытка прочитать данные по url. Если смогли прочитать данные, то
        //создаем документа, читаем статистику и записываем в базу
        // А если не смогли то выдать ошибку на страницу(НЕ удалось прочитать данные по указанному url)

        return "redirect:/";
    }

    @GetMapping("/document/{id}")
    public String view(@PathVariable("id") long id,
                       Model model) {

        model.addAttribute("document", documentEntityRepository.findById(id).get());
        model.addAttribute("words", wordStatEntityRepository.findAllByDocumentId(id));
        return "document/view";
    }
}

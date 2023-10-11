package org.example.app.document.create;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.app.document.DocumentEntityRepository;
import org.example.app.document.DocumentFilter;
import org.example.entity.DocumentEntity;
import org.example.entity.User;
import org.example.entity.WordStatEntity;
import org.example.normalizer.LongWordsNormalizer;
import org.example.normalizer.Normalizer;
import org.example.normalizer.NormalizerCollection;
import org.example.normalizer.ShortWordsNormalizer;
import org.example.app.word.WordStatEntityRepository;
import org.example.service.*;
import org.example.util.flash.FlashMessage;
import org.example.util.flash.FlashUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class DocumentCreateController {

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
            Model model,
            Principal principal,
            RedirectAttributes redirectAttributes) {

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
        documentEntity.setUser((User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal());

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

        //todo Transaction ??? SKILLBOX
        //FlashUtils.addMessage(redirectAttributes, "document.created", FlashMessage.Type.SUCCESS);
        documentEntityRepository.save(documentEntity);

        try {
            wordStatEntityRepository.saveAll(wordStatEntityList);
        } catch (UrlDataLoadException e) {
            ObjectError error = new ObjectError("document", "???");
            bindingResult.rejectValue("url", "value.negative", new ObjectError[]{error}, "Ошибка записи данных");
            model.addAttribute("document", documentCreateDto);
            return "document/add";
        }

        redirectAttributes.addFlashAttribute("flashMessage",
                "Документ \"" + documentCreateDto.getName() + "\" успешно создан");
        return "redirect:/document";
    }


}

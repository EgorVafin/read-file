package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.repository.SummaryWordStat;
import org.example.repository.WordStatEntityRepository;
import org.example.service.WordCommonCounter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GeneralController {
    private final WordStatEntityRepository wordStatEntityRepository;
    private final DocumentEntityRepository documentEntityRepository;
    private final WordCommonCounter wordCommonCounter;

    @GetMapping("/")
    public String index(@RequestParam(required = false, defaultValue = "1") Integer page,
                        @RequestParam(required = false, defaultValue = "20") Integer perPage,
                        Model model) {

        Pageable paging = PageRequest.of(page - 1, perPage);
        Page<SummaryWordStat> summaryWordStatList = wordStatEntityRepository.findCommonWordsStat(paging);
        model.addAttribute("words", summaryWordStatList);

        return "index";
    }

}

package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.WordAndCount;
import org.example.service.WordAndCountBusinessLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileNotFoundException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GeneralController {
    private final WordAndCountBusinessLogic logic;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/read")
    public String read(Model model) throws FileNotFoundException {
        logic.read();
        return "read";
    }

    @GetMapping("/save")
    public String save(Model model) throws FileNotFoundException {
        logic.save();
        return "save";
    }

    @GetMapping("/view")
    public String view(Model model) {
        model.addAttribute("words", logic.view());
        return "view";
    }

}

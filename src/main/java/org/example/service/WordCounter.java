package org.example.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WordCounter {
    private final Map<String, Integer> wordsStat = new HashMap<>();

    public WordCounter add(String word) {

        if (!word.isBlank()) {
            word = word.toLowerCase();
            wordsStat.put(word, wordsStat.containsKey(word) ? wordsStat.get(word) + 1 : 1);
        }
        return this;
    }

    public WordCounter addAll(List<String> words) {

        words.forEach(this::add);
        return this;
    }

    public List<WordStat> getOrderedStat() {

        return wordsStat.entrySet()
                .stream()
                .map(entry -> new WordStat(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> Integer.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());
    }
}

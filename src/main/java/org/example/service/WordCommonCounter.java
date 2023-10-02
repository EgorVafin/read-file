package org.example.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.example.entity.WordStatEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class WordCommonCounter {

    private final Map<String, Integer> wordsStat = new HashMap<>();

    public List<WordStat> addAll(List<WordStatEntity> wordStatEntityList) {

        Map<String, Integer> wordsStat = new HashMap<>();

        for (WordStatEntity entity : wordStatEntityList) {
            wordsStat.put(entity.getWord(), wordsStat.containsKey(entity.getWord())
                    ? wordsStat.get(entity.getWord()) + entity.getCount() : entity.getCount());
        }
        List<WordStat> orderedWordStat = wordsStat.entrySet()
                .stream()
                .map(entry -> new WordStat(entry.getKey(), entry.getValue()))
                .sorted((a, b) -> Integer.compare(b.getCount(), a.getCount()))
                .collect(Collectors.toList());

        return orderedWordStat;
    }
}

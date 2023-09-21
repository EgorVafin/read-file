package org.example.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LineToWordsSplitter {

    public List<String> split(String line) {
        String[] wordsFromLine = line.split("[ .,!?;:]+");

        return List.of(wordsFromLine);
    }
}

package org.example.service;

import org.example.normalizer.Normalizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class WordFileReaderByFileAndScanner {
    private final Scanner scanner;
    private final String fileName;
    private final LineToWordsSplitter splitter;
    private boolean scannerIsOpen;
    private final File file;
    private final Normalizer normalizer;

    private final WordJsoupParser wordJsoupParser;

    public WordFileReaderByFileAndScanner(
            String fileName,
            LineToWordsSplitter splitter,
            WordJsoupParser wordJsoupParser,
            Normalizer normalizer) throws FileNotFoundException {
        this.fileName = fileName;
        this.splitter = splitter;
        this.file = new File(fileName);
        this.scanner = new Scanner(new BufferedReader(new FileReader(file)));
        this.scannerIsOpen = true;
        this.wordJsoupParser = wordJsoupParser;
        this.normalizer = normalizer;
    }

    public List<String> readWords() {
        if (!scannerIsOpen) {
            return List.of();
        }

        while (true) {
            if (!scanner.hasNextLine()) {
                scanner.close();
                scannerIsOpen = false;
                return List.of();
            }

            String line = scanner.nextLine();
            String clearedLine = wordJsoupParser.cleanHtmlTags(line);
            List<String> words = splitter.split(clearedLine);

            words = normalizer.normalize(words);

            if (!words.isEmpty()) {
                return words;
            }
        }
    }
}


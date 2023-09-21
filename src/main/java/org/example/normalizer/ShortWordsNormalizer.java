package org.example.normalizer;

import java.util.List;

public class ShortWordsNormalizer implements Normalizer {
    private final int wordLength;

    public ShortWordsNormalizer(int wordLength) {
        this.wordLength = wordLength;
    }

    @Override
    public List<String> normalize(List<String> words) {

        return words.stream().filter(a -> a.length() > wordLength).toList();

    }
}

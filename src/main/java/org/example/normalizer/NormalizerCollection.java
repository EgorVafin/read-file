package org.example.normalizer;

import java.util.List;

public class NormalizerCollection implements Normalizer {
    private final List<Normalizer> normalizers;

    public NormalizerCollection(List<Normalizer> normalizers) {
        this.normalizers = normalizers;
    }

    @Override
    public List<String> normalize(List<String> words) {
        for (Normalizer normalizer : normalizers) {
            words = normalizer.normalize(words);
        }

        return words;
    }
}

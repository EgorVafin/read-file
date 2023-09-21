package org.example.service;

public class WordStat {
    public final String word;
    public final int count;

    public WordStat(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }
}

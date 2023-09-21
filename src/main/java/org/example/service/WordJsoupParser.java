package org.example.service;

import org.jsoup.Jsoup;

public class WordJsoupParser {

    public WordJsoupParser() {
    }

    public String cleanHtmlTags(String word) {

        return Jsoup.parse(word).text();
    }
}

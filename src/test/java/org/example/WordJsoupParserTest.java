package org.example;

import org.example.service.WordJsoupParser;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordJsoupParserTest {

    @Test
    public void testCleanHtmlTags() {
        String testLine = "World hello!<p></p>Hello world";
        WordJsoupParser parser = new WordJsoupParser();
        String cleanedLine = parser.cleanHtmlTags(testLine);

        assertEquals("World hello! Hello world", cleanedLine);
    }

    @Test
    public void testOnlyHtmlTags() {
        String testLine = "<p></p><a><body></body>";
        WordJsoupParser parser = new WordJsoupParser();
        String cleanedLine = parser.cleanHtmlTags(testLine);

        assertEquals("", cleanedLine);
    }

    @Test
    public void testHtmlTagsWithAttributes() {
        String testLine = "<html lang=\"RU\"><body class=\"new-data\"><p>Hello</p></body></html>";
        WordJsoupParser parser = new WordJsoupParser();
        String cleanedLine = parser.cleanHtmlTags(testLine);

        assertEquals("Hello", cleanedLine);
    }

}
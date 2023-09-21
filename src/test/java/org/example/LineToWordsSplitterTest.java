package org.example;

import junit.framework.TestCase;
import org.example.service.LineToWordsSplitter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class LineToWordsSplitterTest extends TestCase {

    @Test
    public void testSpaceDelimiters() {
        String testLine = "word1 word2 word3";
        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();

        Assert.assertEquals(List.of("word1", "word2", "word3"), lineToWordsSplitter.split(testLine));
    }
    //todo check for other delimiters, multiple delimiters,  empty line

    @Test
    public void testCommaDelimiters() {
        String testLine = "word1,word2,word3";
        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();

        Assert.assertEquals(List.of("word1", "word2", "word3"), lineToWordsSplitter.split(testLine));
    }

    @Test
    public void testOneDifferentDelimiters() {
        String testLine = "word1,word2 word3:word4";
        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();

        Assert.assertEquals(List.of("word1", "word2", "word3", "word4"), lineToWordsSplitter.split(testLine));
    }

    @Test
    public void testTwoDifferentDelimiters() {
        String testLine = "word1, word2! word3: word4? word5.";
        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();

        Assert.assertEquals(List.of("word1", "word2", "word3", "word4", "word5"), lineToWordsSplitter.split(testLine));
    }

    @Test
    public void testEmptyLine() {
        String testLine = "";
        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();

        Assert.assertEquals(List.of(), lineToWordsSplitter.split(testLine));
    }

    @Test
    public void testMultipleDelimiters() {
        String testLine = ",,,,,.....,,,,      ,     ";
        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();

        assertEquals(List.of(), lineToWordsSplitter.split(testLine));
    }

}
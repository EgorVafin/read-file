package org.example;

import junit.framework.TestCase;
import org.example.service.WordCounter;
import org.example.service.WordStat;

import java.util.List;

public class WordCounterTest extends TestCase {

    public void testOneWordTest(){
        WordCounter wc = new WordCounter();
        wc.add("one");
        List<WordStat> stat = wc.getOrderedStat();

        assertEquals(1, stat.size());
    }

    public void testOrderStat(){
        WordCounter wc = new WordCounter();
        wc.addAll(List.of("three", "one", "two", "one"));

        List<WordStat> stat = wc.getOrderedStat();
        assertEquals("one", stat.get(0).getWord());
    }

    public void testMultipleWordsStat(){
        WordCounter wc = new WordCounter();
        wc.add("one").add("one");

        List<WordStat> stat = wc.getOrderedStat();
        assertEquals(2, stat.get(0).getCount());
    }
}
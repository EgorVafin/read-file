package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileNotFoundException;

@SpringBootApplication
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        SpringApplication.run(Main.class, args);

//        args[0] = "v_m.html";
//        args[1] = "out.csv";
//
//        if (args.length != 2) {
//            System.err.println("Usage: inputFile outputFile");
//            System.exit(-1);
//        }

//        String fileNameForRead = "C:\\JAVA\\FileToRead.txt";
//        String fileNameForSave = "C:\\JAVA\\out.csv";
//
//        File file = new File(fileNameForRead);
//        if (!file.exists() || !file.canRead()) {
//            System.err.println("Invalid input file");
//            System.exit(-1);
//        }
//
//        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();
//        WordJsoupParser wordJsoupParser = new WordJsoupParser();
//        WordFileReaderByFileAndScanner reader = new WordFileReaderByFileAndScanner(
//                fileNameForRead,
//                lineToWordsSplitter,
//                wordJsoupParser,
//                new NormalizerCollection(List.of(
//                        new ShortWordsNormalizer(2),
//                        new LongWordsNormalizer(15)
//                ))
//        );
//        WordCounter wordCounter = new WordCounter();
//
//        while (true) {
//            final List<String> wordsList = reader.readWords();
//            if (wordsList.isEmpty()) {
//                break;
//            }
//            wordCounter.addAll(wordsList);
//        }
//        List<WordStat> wordStatList = wordCounter.getOrderedStat();
//
//        WordStatSaver saver = new SaveToCsvFileByPrintWriter(fileNameForSave);
//        WordStatSaver saver1 = new SaveToDbMySql();
//        saver.save(wordStatList);
//        saver1.save(wordStatList);
    }
}
package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.WordAndCount;
import org.example.normalizer.LongWordsNormalizer;
import org.example.normalizer.NormalizerCollection;
import org.example.normalizer.ShortWordsNormalizer;
import org.example.repository.WordAndCountRepository;
import org.example.save.SaveToCsvFile;
import org.example.save.SaveToDbMySql;
import org.example.save.WordStatSaver;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WordAndCountBusinessLogic {
    private final SaveToDbMySql saveToDbMySql;
    private final WordCounter wordCounter;
    private final WordAndCountRepository repository;


    public void read() throws FileNotFoundException {

        String fileNameForRead = "C:\\JAVA\\FileToRead.txt";
        String fileNameForSave = "C:\\JAVA\\out.csv";

        File file = new File(fileNameForRead);
        if (!file.exists() || !file.canRead()) {
            System.err.println("Invalid input file");
            System.exit(-1);
        }

        LineToWordsSplitter lineToWordsSplitter = new LineToWordsSplitter();
        WordJsoupParser wordJsoupParser = new WordJsoupParser();
        WordFileReaderByFileAndScanner reader = new WordFileReaderByFileAndScanner(
                fileNameForRead,
                lineToWordsSplitter,
                wordJsoupParser,
                new NormalizerCollection(List.of(
                        new ShortWordsNormalizer(2),
                        new LongWordsNormalizer(15)
                ))
        );

        while (true) {
            final List<String> wordsList = reader.readWords();
            if (wordsList.isEmpty()) {
                break;
            }

            wordCounter.addAll(wordsList);
        }

    }

    public void save() throws FileNotFoundException {

        List<WordStat> wordStatList = wordCounter.getOrderedStat();

        repository.deleteAll();

        WordStatSaver saver1 = saveToDbMySql;
        saver1.save(wordStatList);

        //WordStatSaver saver = new SaveToCsvFile(fileNameForSave);
        //saver.save(wordStatList);
    }

    public List<WordAndCount> view() {

        return repository.findAll();
    }

}

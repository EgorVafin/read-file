package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.WordStatEntity;
import org.example.normalizer.LongWordsNormalizer;
import org.example.normalizer.NormalizerCollection;
import org.example.normalizer.ShortWordsNormalizer;
import org.example.repository.WordStatEntityRepository;
import org.example.save.SaveToDbMySql;
import org.example.save.WordStatSaver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WordStatReadFromFile {
    private final SaveToDbMySql saveToDbMySql;
    private final WordCounter wordCounter;
    private final WordStatEntityRepository repository;


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

        List<org.example.service.WordStat> wordStatList = wordCounter.getOrderedStat();

        repository.deleteAll(); //???????????

        WordStatSaver saver1 = saveToDbMySql;
        saver1.save(wordStatList);

        //WordStatSaver saver = new SaveToCsvFile(fileNameForSave);
        //saver.save(wordStatList);
    }

    public Page<WordStatEntity> view(Integer offset, Integer limit) {
        Pageable paging = PageRequest.of(offset, limit);

        return repository.findAll(paging);
    }

}

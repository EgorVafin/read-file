package org.example.save;

import org.example.service.WordStat;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

public class SaveToCsvFile implements WordStatSaver {
    private final String fileName;

    public SaveToCsvFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void save(List<WordStat> wordStatList) throws WordStatSaveException {
        try (PrintWriter out = new PrintWriter(fileName)) {
            wordStatList.forEach(word -> out.printf("%s;%d\n", word.getWord(), word.getCount()));
        } catch (FileNotFoundException e) {
            throw new WordStatSaveException(e);
        }
    }
}

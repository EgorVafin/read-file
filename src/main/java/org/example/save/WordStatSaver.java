package org.example.save;

import lombok.NoArgsConstructor;
import org.example.service.WordStat;

import java.util.List;

public interface WordStatSaver {
    void save(List<WordStat> list) throws WordStatSaveException;
}

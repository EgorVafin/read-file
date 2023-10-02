package org.example.save;

import jakarta.transaction.Transactional;
import org.example.service.WordStat;


import java.util.List;

public interface WordStatSaver {


    void save(List<WordStat> list) throws WordStatSaveException;
}

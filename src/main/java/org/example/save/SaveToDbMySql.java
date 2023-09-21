package org.example.save;

import lombok.RequiredArgsConstructor;
import org.example.entity.WordAndCount;
import org.example.repository.WordAndCountRepository;
import org.example.service.WordStat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveToDbMySql implements WordStatSaver {
    private final WordAndCountRepository repository;
    private final List<WordAndCount> entityList;

    @Override
    public void save(List<WordStat> list) throws WordStatSaveException {

        for (WordStat word : list) {
            WordAndCount entity = new WordAndCount();
            entity.setWord(word.getWord());
            entity.setCount(word.getCount());
            entityList.add(entity);
        }

        repository.saveAll(entityList);

    }
}

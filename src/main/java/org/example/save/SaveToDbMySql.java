package org.example.save;

import lombok.RequiredArgsConstructor;
import org.example.entity.WordStatEntity;
import org.example.repository.WordStatEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaveToDbMySql implements WordStatSaver {
    private final WordStatEntityRepository repository;
    private final List<WordStatEntity> entityList;

    @Override
    public void save(List<org.example.service.WordStat> list) throws WordStatSaveException {

        for (org.example.service.WordStat word : list) {
            WordStatEntity entity = new WordStatEntity();
            entity.setWord(word.getWord());
            entity.setCount(word.getCount());
            entityList.add(entity);
        }

        repository.saveAll(entityList); // ?????????????

    }
}

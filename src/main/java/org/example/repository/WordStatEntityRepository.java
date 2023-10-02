package org.example.repository;

import org.example.controller.WordStatListDto;
import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.example.util.validation.match.FieldsValueMatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordStatEntityRepository extends JpaRepository<WordStatEntity, Long>,
        PagingAndSortingRepository<WordStatEntity, Long> {

    Page<WordStatEntity> findAll(Pageable pageable);

    @Query(value = "SELECT word as name, SUM(count) as 'count' FROM word_stat GROUP BY word", nativeQuery = true)
    public List<WordStatEntity> findCommonWordsStat();

    //@Query(value = "SELECT word as name, SUM(count) as 'count' FROM word_stat GROUP BY word", nativeQuery = true)
    public List<WordStatEntity> findAllByDocumentId(long id);
}

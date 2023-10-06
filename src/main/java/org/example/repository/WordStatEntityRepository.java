package org.example.repository;

import org.example.entity.WordStatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordStatEntityRepository extends JpaRepository<WordStatEntity, Long>,
        PagingAndSortingRepository<WordStatEntity, Long> {

    Page<WordStatEntity> findAll(Pageable pageable);

    @Query(value = "SELECT count(document_id) as docCount,word as word, SUM(count) as frequency FROM word_stat WHERE word like %:word% GROUP BY word order by frequency DESC",
            countQuery = "select count(distinct word) from word_stat",
            nativeQuery = true)
    public Page<SummaryWordStat> findCommonWordsStat1(String word, Pageable page);

    @Query(value = "SELECT count(document_id) as docCount,word as word, SUM(count) as frequency FROM word_stat GROUP BY word order by frequency DESC",
            countQuery = "select count(distinct word) from word_stat",
            nativeQuery = true)
    public Page<SummaryWordStat> findCommonWordsStat(Pageable page);


    //@Query(value = "SELECT word as name, SUM(count) as 'count' FROM word_stat GROUP BY word", nativeQuery = true)
    public List<WordStatEntity> findAllByDocumentId(long id);
}

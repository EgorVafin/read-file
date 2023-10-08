package org.example.app.word;

import org.example.entity.DocumentEntity;
import org.example.entity.WordStatEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordStatEntityRepository extends JpaRepository<WordStatEntity, Long>,
        PagingAndSortingRepository<WordStatEntity, Long>,
        JpaSpecificationExecutor<WordStatEntity> {

    static Specification<WordStatEntity> wordNameLike(String word) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("word"), "%" + word + "%");
    }

    static Specification<WordStatEntity> wordFrequencyEqual(Integer frequency) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.equal(root.get("count"), frequency);

    }

    static Specification<WordStatEntity> documentsIn(List<DocumentEntity> document) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.in(root.get("document")).value(document);
    }

    @Query(value = "SELECT count(document_id) as docCount,word as word, SUM(count) as frequency\n" +
            "FROM word_stat GROUP BY word having word in :words order by frequency DESC",
            countQuery = "select count(distinct word) from word_stat",
            nativeQuery = true)
    public Page<SummaryWordStat> findCommonWordsStatAfterFilter(Pageable page, List<String> words);

    Page<WordStatEntity> findAll(Pageable pageable);

//    @Query(value = "SELECT count(document_id) as docCount,word as word, SUM(count) as frequency FROM word_stat WHERE word like %:word% GROUP BY word order by frequency DESC",
//            countQuery = "select count(distinct word) from word_stat",
//            nativeQuery = true)
//    public Page<SummaryWordStat> findCommonWordsStat1(String word, Pageable page);

    //todo countQuery
    @Query(value = "SELECT count(document_id) as docCount,word as word, SUM(count) as frequency FROM word_stat GROUP BY word order by frequency DESC",
            countQuery = "select count(distinct word) from word_stat",
            nativeQuery = true)
    public Page<SummaryWordStat> findCommonWordsStat(Pageable page);


//    @Query(value = "SELECT word as word,  count as count, document_id FROM word_stat WHERE document_id = 1", nativeQuery = true)
//    List <WordStatEntity> findAllByDocumentId(long id);

    Page <WordStatEntity> findByDocument(Pageable page, Optional<DocumentEntity> document);

}

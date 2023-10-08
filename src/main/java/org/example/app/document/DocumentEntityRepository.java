package org.example.app.document;

import org.example.entity.DocumentEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface DocumentEntityRepository extends JpaRepository<DocumentEntity, Long>,
        PagingAndSortingRepository<DocumentEntity, Long>,
        JpaSpecificationExecutor<DocumentEntity> {

    Optional<Object> findByNameAndIdNot(String name, long id);

    Optional<Object> findByName(String name);

    Optional<DocumentEntity> findByUrl(String url);

    static Specification<DocumentEntity> documentNameLike(String name) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    static Specification<DocumentEntity> documentUrlLike(String url) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(root.get("url"), "%" + url + "%");
    }

    static Specification<DocumentEntity> customersIn(List<DocumentEntity> documents) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.in(root.get("document")).value(documents);
    }

}

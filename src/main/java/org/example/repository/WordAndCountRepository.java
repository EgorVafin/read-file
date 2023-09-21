package org.example.repository;

import org.example.entity.WordAndCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordAndCountRepository extends JpaRepository<WordAndCount, Long> {

    public void deleteByCountNot(int count);

}

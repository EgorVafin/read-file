package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "word_stat")
@Getter
@Setter
@NoArgsConstructor
public class WordAndCount {

    @Id
    @Column(name = "name")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "word")
    private String word;

    @Column(name = "count")
    private int count;
}

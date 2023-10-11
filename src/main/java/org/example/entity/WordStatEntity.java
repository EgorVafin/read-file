package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "word_stat",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"word", "document_id"})
)
@Getter
@Setter
@NoArgsConstructor
@BatchSize(size = 100)
public class WordStatEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "word")
    private String word;

    @Column(name = "count")
    private int count;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "document_id", referencedColumnName = "id", nullable = false)
    private DocumentEntity document;
}

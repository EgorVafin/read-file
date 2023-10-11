package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "document")
@Getter
@Setter
@NoArgsConstructor
public class DocumentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "url", unique = true, length = 512)
    private String url;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    @Column(name = "scraped_at")
    private Date scrapedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "document")
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Set<WordStatEntity> wordStatEntities;

}

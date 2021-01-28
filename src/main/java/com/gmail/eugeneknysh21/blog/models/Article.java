package com.gmail.eugeneknysh21.blog.models;

import com.gmail.eugeneknysh21.blog.auditor.AuditListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn()
    private User author;

    private String title;
    private String section;
    private String anons;
    private String fullText;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdDate;
    private int views;

    public Article(User author, String title, String section, String anons, String fullText) {
        this.author = author;
        this.title = title;
        this.section = section;
        this.anons = anons;
        this.fullText = fullText;
    }
}

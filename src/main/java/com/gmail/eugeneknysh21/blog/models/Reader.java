package com.gmail.eugeneknysh21.blog.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uuid;

    @Column(nullable = false)
    private Long articleId;

    public Reader(String uuid, Long articleId) {
        this.uuid = uuid;
        this.articleId = articleId;
    }
}

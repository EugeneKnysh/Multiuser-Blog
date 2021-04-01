package com.gmail.eugeneknysh21.blog.repository;

import com.gmail.eugeneknysh21.blog.models.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    Page<Article> findBySection(Pageable pageable, String section);

    Page<Article> findByAuthorId(Pageable pageable, Long authorId);

    @Query("SELECT a FROM Article a WHERE a.title LIKE %:searchText% OR a.anons LIKE %:searchText% OR a.fullText LIKE %:searchText%")
    Page<Article> findArticlesByLike(Pageable pageable, String searchText);
}

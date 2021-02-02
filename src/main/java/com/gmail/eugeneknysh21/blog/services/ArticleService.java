package com.gmail.eugeneknysh21.blog.services;

import com.gmail.eugeneknysh21.blog.dto.ArticleDTO;
import com.gmail.eugeneknysh21.blog.dto.PageDTO;
import org.springframework.data.domain.Sort;


import java.util.Collection;

public interface ArticleService {
    Long create(ArticleDTO articleDTO);

    ArticleDTO getArticleById(Long id);

    Collection<ArticleDTO> getAll();

    PageDTO<ArticleDTO> getAll(Integer page, Integer size, Sort.Direction direction, String sortField);

    PageDTO<ArticleDTO> getAllBySection(Integer page, Integer size, Sort.Direction direction, String sortField, String section);

    PageDTO<ArticleDTO> getAllByAuthorId(Long authorId, Integer page, Integer size, Sort.Direction direction, String sortField);

    boolean update(ArticleDTO articleDTO);

    boolean remove(Long id);

    void incViewsById(Long id);
}

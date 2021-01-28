package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.dto.ArticleDTO;
import com.gmail.eugeneknysh21.blog.models.Article;
import com.gmail.eugeneknysh21.blog.repository.ArticleRepository;
import com.gmail.eugeneknysh21.blog.services.ArticleService;
import com.gmail.eugeneknysh21.blog.utility.PageableCreator;
import com.gmail.eugeneknysh21.blog.dto.PageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    @Override
    public Long create(ArticleDTO articleDTO) {
        Article article = new Article(articleDTO.getAuthor(),
                articleDTO.getTitle(),
                articleDTO.getSection(),
                articleDTO.getAnons(),
                articleDTO.getFullText());

        article = articleRepository.save(article);
        return article.getId();
    }

    @Override
    public ArticleDTO getArticleById(Long id) {
        return articleRepository.findById(id)
                .map(this::getArticleDTO)
                .orElseThrow(() -> new EntityNotFoundException("Article " + id + " not found!"));
    }

    @Override
    public Collection<ArticleDTO> getAll() {
        return articleRepository.findAll().stream()
                .map(this::getArticleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PageDTO<ArticleDTO> getAll(Integer page, Integer size, Sort.Direction direction, String sortField) {
        Page<Article> articlePage = articleRepository.findAll(PageableCreator.getPageable(page, size, direction, sortField));
        return PageDTO.convertToPageDto(articlePage, this::getArticleDTO);
    }

    @Override
    public PageDTO<ArticleDTO> getAllBySection(Integer page, Integer size, Sort.Direction direction, String sortField, String section) {
        Page<Article> articlePage = articleRepository.findBySection(PageableCreator.getPageable(page, size, direction, sortField), section);
        return PageDTO.convertToPageDto(articlePage, this::getArticleDTO);
    }

    private ArticleDTO getArticleDTO(Article article) {
        return new ArticleDTO(
                article.getId(),
                article.getAuthor(),
                article.getTitle(),
                article.getSection(),
                article.getAnons(),
                article.getFullText(),
                article.getCreatedDate(),
                article.getViews()
        );
    }

    @Override
    public Long update(ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("Article can`t be updated. Article not found."));
        article.setTitle(articleDTO.getTitle());
        article.setAnons(articleDTO.getAnons());
        article.setFullText(articleDTO.getFullText());
        article.setSection(articleDTO.getSection());

        article = articleRepository.save(article);
        return article.getId();
    }

    @Override
    public boolean remove(Long id) {
        articleRepository.deleteById(id);
        return true;
    }

    @Override
    public void incViewsById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Article doesn`t exist."));
        int views = article.getViews() + 1;
        article.setViews(views);
        articleRepository.save(article);
    }
}

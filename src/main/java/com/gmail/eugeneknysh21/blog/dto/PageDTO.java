package com.gmail.eugeneknysh21.blog.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.function.Function;

@Getter
public class PageDTO<U> {
    private final Collection<U> content;
    private final int totalPages;
    private final long totalElements;

    private PageDTO(Page<U> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.content = page.getContent();
    }

    public static <T, U> PageDTO<U> convertToPageDto(Page<T> all, Function<T, U> mapFunction) {
        return new PageDTO<>(all.map(mapFunction));
    }
}

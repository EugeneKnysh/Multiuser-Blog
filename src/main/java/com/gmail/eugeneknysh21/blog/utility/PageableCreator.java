package com.gmail.eugeneknysh21.blog.utility;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableCreator {

    public static Pageable getPageable(Integer page, Integer size, Sort.Direction direction, String sortField) {
        return PageRequest.of(
                page == null ? 0 : page,
                size == null ? 10 : size,
                direction == null ? Sort.Direction.DESC : direction,
                sortField);
    }

    public static Pageable getPageable(String sortField) {
        return getPageable(null, null, null, sortField);
    }
}

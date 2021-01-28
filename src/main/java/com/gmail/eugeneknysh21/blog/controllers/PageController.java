package com.gmail.eugeneknysh21.blog.controllers;

import com.gmail.eugeneknysh21.blog.utility.RoleFinder;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class PageController {

    @GetMapping("/")
    public String indexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        if (!RoleFinder.hasRoleUser("ROLE_ANONYMOUS")) {
            return "index";
        }
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        if (!RoleFinder.hasRoleUser("ROLE_ANONYMOUS")) {
            return "index";
        }
        return "registration";
    }

    @GetMapping("/article/add")
    @PreAuthorize("hasAuthority('article:create')")
    public String articleAdd() {
        return "article-add";
    }

    @GetMapping("/articles/section")
    @PreAuthorize("hasAuthority('article:read')")
    public String articlesSection() {
        return "articles-section";
    }

    @GetMapping("/registration/confirm")
    public String confirmPage() {
        return "login";
    }

//    @GetMapping("/search")
//    public String search() {
//        return "search";
//    }

//    @GetMapping("/article/{id}")
//    public String articleGet(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("articleId", id);
//        return "articleGet";
//    }

//    @GetMapping("/article/{id}/edit")
//    public String articleEdit(@PathVariable("id") Long articleId, Model model) {
//        model.addAttribute("articleId", articleId);
//        return "articleEdit";
//    }
}

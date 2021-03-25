package com.gmail.eugeneknysh21.blog.controllers;

import com.gmail.eugeneknysh21.blog.utility.RoleCheck;
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
        if (!RoleCheck.isAnonymous()) {
            return "index";
        }
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        if (!RoleCheck.isAnonymous()) {
            return "index";
        }
        return "registration";
    }

    @GetMapping("/article/add")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public String articleAdd() {
        return "article-add";
    }

    @GetMapping("/articles/section")
    public String articlesSection() {
        return "articles-section";
    }

    @GetMapping("/registration/confirm")
    public String confirmPage() {
        return "login";
    }

    @GetMapping("/articles/my")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public String myArticles() {
        return "my-articles";
    }

    @GetMapping("/article/edit")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public String editArticle() {
        return "article-edit";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public String profile() {
        return "my-profile";
    }

    @GetMapping("/post")
    public String article() {
        return "article";
    }

    @GetMapping("/author")
    public String author() {
        return "authors-articles";
    }
}

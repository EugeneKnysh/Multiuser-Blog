package com.gmail.eugeneknysh21.blog.controllers;

import com.gmail.eugeneknysh21.blog.dto.ArticleDTO;
import com.gmail.eugeneknysh21.blog.dto.PageDTO;
import com.gmail.eugeneknysh21.blog.dto.UserDTO;
import com.gmail.eugeneknysh21.blog.services.ArticleService;
import com.gmail.eugeneknysh21.blog.services.UserService;
import com.gmail.eugeneknysh21.blog.utility.RoleFinder;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequiredArgsConstructor
public class BlogController {
    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping("/getArticlePages")
    public PageDTO<ArticleDTO> getArticlePages(@RequestParam(required = false) Integer page,
                                               @RequestParam(required = false) Integer size,
                                               @RequestParam(required = false) Sort.Direction direction,
                                               @RequestParam String sortField) {
        return articleService.getAll(page, size, direction, sortField);
    }

    @GetMapping("/article/all")
    @PreAuthorize("hasAuthority('article:read')")
    public PageDTO<ArticleDTO> getAllArticleByAuthor(@RequestParam(required = false) Long authorId,
                                                     @RequestParam(required = false) Integer page,
                                                     @RequestParam(required = false) Integer size,
                                                     @RequestParam(required = false) Sort.Direction direction,
                                                     @RequestParam String sortField) {
        return articleService.getAllByAuthorId(authorId, page, size, direction, sortField);
    }

    @GetMapping("/article")
    public ArticleDTO getArticle(@RequestParam Long id) {
        return articleService.getArticleById(id);
    }

    @PostMapping("/article/add")
    @PreAuthorize("hasAuthority('article:read')")
    public Long addArticle(@Valid @RequestBody ArticleDTO articleDTO) {
        return articleService.create(articleDTO);
    }

    @GetMapping("/article/sectionPages")
    public PageDTO<ArticleDTO> getArticlesBySection(@RequestParam(required = false) Integer page,
                                                    @RequestParam(required = false) Integer size,
                                                    @RequestParam(required = false) Sort.Direction direction,
                                                    @RequestParam String sortField,
                                                    @RequestParam String section) {
        return articleService.getAllBySection(page, size, direction, sortField, section);
    }

    @GetMapping("/role")
    public boolean hasRole(@RequestParam String role) {
        return RoleFinder.hasRoleUser(role);
    }

    @GetMapping("/principal")
    public UserDTO getPrincipal() {
        return userService.getPrincipal();
    }

    @PostMapping("/registration")
    public Long registration(@RequestBody @Valid UserDTO userDTO) {
        return userService.registerUser(userDTO);
    }

    @GetMapping("/registrationConfirm")
    public boolean registrationConfirm(@RequestParam String token) {
        return userService.confirmRegistration(token);
    }

    @GetMapping("/resendConfirmLink")
    public boolean resendConfirmLink(@RequestParam String token) {
        return userService.resendConfirmLink(token);
    }

    @GetMapping("/checkEmail")
    public boolean checkEmailIdentity(@RequestParam String email) {
        return userService.checkEmailIdentity(email);
    }

    @GetMapping("/checkAlias")
    public boolean checkAliasIdentity(@RequestParam String alias) {
        return userService.checkAliasIdentity(alias);
    }

    @DeleteMapping("/article")
    @PreAuthorize("hasAuthority('article:read')")
    public boolean deleteArticle(@RequestParam Long id) {
        return articleService.remove(id);
    }














    @GetMapping("/NoSuchElement")
    public void noSuchElement() {
        throw new NoSuchElementException("Test Message in NoSuchElementException.");
    }

    @GetMapping("/mail")
    public void mailTest() {
        throw new MailParseException("Test Message in MailParseException.");
    }

    @GetMapping("/noHandle")
    public void noHandle() throws NotFoundException {
        throw new NotFoundException("Test Message in NotFoundException.");
    }
}

package com.gmail.eugeneknysh21.blog.controllers;

import com.gmail.eugeneknysh21.blog.dto.ArticleDTO;
import com.gmail.eugeneknysh21.blog.dto.PageDTO;
import com.gmail.eugeneknysh21.blog.dto.UserDTO;
import com.gmail.eugeneknysh21.blog.dto.UserDataDTO;
import com.gmail.eugeneknysh21.blog.services.ArticleService;
import com.gmail.eugeneknysh21.blog.services.UserService;
import com.gmail.eugeneknysh21.blog.utility.RoleCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

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

    @GetMapping("/increment")
    public void incrementView(@RequestParam Long id, @CookieValue(name = "UUID", required = false) Cookie cookieUuid, HttpServletResponse response) {
        if (cookieUuid == null) {
            cookieUuid = new Cookie("UUID", articleService.incViewsById(id, null));
            cookieUuid.setHttpOnly(true);
            response.addCookie(cookieUuid);
        } else {
            articleService.incViewsById(id, cookieUuid.getValue());
        }
    }

    @PostMapping("/article/add")
    @PreAuthorize("hasAuthority('OP_CREATE')")
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

    @GetMapping("/article/search")
    public PageDTO<ArticleDTO> searchArticle(@RequestParam(required = false) Integer page,
                                             @RequestParam(required = false) Integer size,
                                             @RequestParam(required = false) Sort.Direction direction,
                                             @RequestParam String sortField,
                                             @RequestParam String searchText) {
        return articleService.searchArticle(page, size, direction, sortField, searchText);
    }

    @DeleteMapping("/article")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public boolean deleteArticle(@RequestParam Long id) {
        return articleService.remove(id);
    }

    @PutMapping("/article/edit")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public boolean editArticle(@RequestBody ArticleDTO articleDTO) {
        return articleService.update(articleDTO);
    }

    @GetMapping("/role")
    public boolean hasRole(@RequestParam String role) {
        return RoleCheck.hasRolePrincipal(role);
    }

    @GetMapping("/isAnonymous")
    public boolean isAnonymous() {
        return RoleCheck.isAnonymous();
    }

    @GetMapping("/principal")
    @PreAuthorize("hasAuthority('OP_CREATE')")
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

    @PutMapping("/user/edit")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public boolean editUserData(@RequestBody @Valid UserDataDTO userDataDTO) {
        return userService.update(userDataDTO);
    }

    @PostMapping("/checkPassword")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public boolean checkPassword(@RequestBody String password) {
        return userService.checkPassword(password);
    }

    @PutMapping("/user/edit/password")
    @PreAuthorize("hasAuthority('OP_CREATE')")
    public boolean editPassword(@RequestBody Map<String, String> body) {
        return userService.updatePassword(Long.parseLong(body.get("id")), body.get("password"));
    }
}

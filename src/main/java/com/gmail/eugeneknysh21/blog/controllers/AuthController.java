package com.gmail.eugeneknysh21.blog.controllers;

import com.gmail.eugeneknysh21.blog.dto.AuthenticationRequestDTO;
import com.gmail.eugeneknysh21.blog.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDTO requestDTO, HttpServletResponse response) {
        try {
            String token = authService.authenticate(requestDTO);
            response.addCookie(authService.getAccessTokenCookie(token));
            return ResponseEntity.ok().build();
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination.", HttpStatus.UNAUTHORIZED);
        }
    }
}

package com.gmail.eugeneknysh21.blog.services;

import com.gmail.eugeneknysh21.blog.dto.AuthenticationRequestDTO;

import javax.servlet.http.Cookie;

public interface AuthService {
    String authenticate(AuthenticationRequestDTO requestDTO);

    Cookie getAccessTokenCookie(String token);
}

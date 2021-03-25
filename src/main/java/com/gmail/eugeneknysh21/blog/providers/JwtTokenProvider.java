package com.gmail.eugeneknysh21.blog.providers;

import org.springframework.security.core.Authentication;

import javax.servlet.http.Cookie;

public interface JwtTokenProvider {
    String createToken(String username);

    boolean validateToken(String token);

    Authentication getAuthentication(String token);

    String getUsername(String token);

    String resolveAccessToken(Cookie[] cookies);
}

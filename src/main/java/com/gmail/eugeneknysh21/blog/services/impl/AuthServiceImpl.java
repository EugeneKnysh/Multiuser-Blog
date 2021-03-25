package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.dto.AuthenticationRequestDTO;
import com.gmail.eugeneknysh21.blog.providers.JwtTokenProvider;
import com.gmail.eugeneknysh21.blog.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.cookies.expiration}")
    private int cookieExpiration;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String authenticate(AuthenticationRequestDTO requestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
        return jwtTokenProvider.createToken(requestDTO.getEmail());
    }

    @Override
    public Cookie getAccessTokenCookie(String token) {
        Cookie accessToken = new Cookie("access_token", token);
        accessToken.setHttpOnly(true);
        accessToken.setMaxAge(cookieExpiration/1000);
        return accessToken;
    }
}

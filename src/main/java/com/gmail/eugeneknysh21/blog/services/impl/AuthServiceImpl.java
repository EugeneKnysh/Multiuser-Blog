package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.dto.AuthenticationRequestDTO;
import com.gmail.eugeneknysh21.blog.providers.JwtTokenProvider;
import com.gmail.eugeneknysh21.blog.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String authenticate(AuthenticationRequestDTO requestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword()));
        return jwtTokenProvider.createToken(requestDTO.getEmail());
    }
}

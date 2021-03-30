package com.gmail.eugeneknysh21.blog.services;

import com.gmail.eugeneknysh21.blog.dto.AuthenticationRequestDTO;

public interface AuthService {
    String authenticate(AuthenticationRequestDTO requestDTO);
}

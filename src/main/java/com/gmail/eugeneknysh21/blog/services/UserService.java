package com.gmail.eugeneknysh21.blog.services;

import com.gmail.eugeneknysh21.blog.dto.UserDTO;
import com.gmail.eugeneknysh21.blog.dto.UserDataDTO;
import com.gmail.eugeneknysh21.blog.models.User;

import java.util.Collection;

public interface UserService {
    Long registerUser(UserDTO userDTO);

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    Collection<UserDTO> getAll();

    UserDTO getUserDTO(User user);

    boolean update(UserDataDTO userDataDTO);

    boolean remove(Long id);

    boolean checkEmailIdentity(String email);

    void sendConfirmMessage(User user);

    boolean confirmRegistration(String token);

    boolean resendConfirmLink(String token);

    boolean checkAliasIdentity(String alias);

    UserDTO getPrincipal();

    User getPrincipalUser();

    boolean checkPassword(String password);

    boolean updatePassword(Long id, String password);
}

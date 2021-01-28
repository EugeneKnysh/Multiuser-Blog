package com.gmail.eugeneknysh21.blog.security;

import com.gmail.eugeneknysh21.blog.models.User;
import com.gmail.eugeneknysh21.blog.models.enums.Status;
import org.springframework.security.core.userdetails.UserDetails;

public final class UserDetailsFactory {
    public static UserDetails createOfUser(User user) {
        return new UserDetailsImpl(
                user.getEmail(),
                user.getPassword(),
                user.getRole().getAuthorities(),
                user.getStatus().equals(Status.ACTIVE),
                user.isEnabled()
        );
    }
}

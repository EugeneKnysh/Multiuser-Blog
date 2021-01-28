package com.gmail.eugeneknysh21.blog.services;

import com.gmail.eugeneknysh21.blog.models.User;
import com.gmail.eugeneknysh21.blog.models.VerificationToken;

public interface VerificationTokenService {
    String createVerificationToken(User user);

    VerificationToken getByToken(String token);

    boolean isExpired(VerificationToken verificationToken);

    void remove(VerificationToken verificationToken);
}

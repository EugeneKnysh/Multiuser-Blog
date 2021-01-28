package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.models.User;
import com.gmail.eugeneknysh21.blog.services.VerificationTokenService;
import com.gmail.eugeneknysh21.blog.models.VerificationToken;
import com.gmail.eugeneknysh21.blog.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository verificationTokenRepository;

    @Override
    public String createVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    @Override
    public VerificationToken getByToken(String token) {
        return verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Verification token not found."));
    }

    @Override
    public boolean isExpired(VerificationToken verificationToken) {
        ZonedDateTime expirationDate = verificationToken.getCreatedDate().plus(24, ChronoUnit.HOURS);
        ZonedDateTime now = ZonedDateTime.now();
        return now.isAfter(expirationDate);
    }

    @Override
    public void remove(VerificationToken verificationToken) {
        verificationTokenRepository.delete(verificationToken);
    }
}

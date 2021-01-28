package com.gmail.eugeneknysh21.blog.auditor;

import com.gmail.eugeneknysh21.blog.models.Article;
import com.gmail.eugeneknysh21.blog.models.VerificationToken;

import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class AuditListener {
    @PrePersist
    private void onPrePersist(Object object) {
        if (object.getClass().equals(Article.class)) {
            Article article = (Article) object;
            article.setCreatedDate(ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC), ZoneOffset.UTC));
        }
        if (object.getClass().equals(VerificationToken.class)) {
            VerificationToken verificationToken = (VerificationToken) object;
            verificationToken.setCreatedDate(ZonedDateTime.of(LocalDateTime.now(ZoneOffset.UTC), ZoneOffset.UTC));
        }
    }
}

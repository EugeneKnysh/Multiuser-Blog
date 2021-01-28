package com.gmail.eugeneknysh21.blog.models;

import com.gmail.eugeneknysh21.blog.auditor.AuditListener;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@EntityListeners(AuditListener.class)
@NoArgsConstructor
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
}

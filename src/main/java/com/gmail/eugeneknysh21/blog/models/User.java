package com.gmail.eugeneknysh21.blog.models;

import com.gmail.eugeneknysh21.blog.models.enums.Status;
import com.gmail.eugeneknysh21.blog.models.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Data
@ToString(exclude = {"articles"})
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String alias;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Collection<Article> articles;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'ROLE_USER'")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'ACTIVE'")
    private Status status;

    @Column(nullable = false)
    private boolean enabled;

    public User(String email, String password, String firstName, String lastName, String alias) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.role = Role.ROLE_USER;
        this.status = Status.ACTIVE;
        this.enabled = false;
    }
}

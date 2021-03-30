package com.gmail.eugeneknysh21.blog.security;

import com.gmail.eugeneknysh21.blog.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Map;

public class OAuth2UserPrincipal implements OidcUser {
    private final User user;

    private final String name;
    private final Map<String, Object> attributes;
    private Map<String, Object> claims;
    private OidcIdToken idToken;
    private OidcUserInfo userInfo;

    public OAuth2UserPrincipal(User user, String name, Map<String, Object> attributes) {
        this.user = user;
        this.name = name;
        this.attributes = attributes;
    }

    public OAuth2UserPrincipal(User user, String name, Map<String, Object> attributes, Map<String, Object> claims, OidcIdToken idToken, OidcUserInfo userInfo) {
        this.user = user;
        this.name = name;
        this.attributes = attributes;
        this.claims = claims;
        this.idToken = idToken;
        this.userInfo = userInfo;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRole().getAuthorities();
    }

    @Override
    public Map<String, Object> getClaims() {
        return this.claims;
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return this.userInfo;
    }

    @Override
    public OidcIdToken getIdToken() {
        return this.idToken;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return user.getEmail();
    }
}

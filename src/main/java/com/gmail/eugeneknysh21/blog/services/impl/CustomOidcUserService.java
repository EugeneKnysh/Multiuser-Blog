package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.security.OAuth2UserPrincipal;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {
    private final CustomOAuth2UserService oAuth2UserService;

    public CustomOidcUserService(CustomOAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        return new OAuth2UserPrincipal(
                oAuth2UserService.buildUser(oidcUser, userRequest),
                oidcUser.getName(),
                oidcUser.getAttributes(),
                oidcUser.getClaims(),
                oidcUser.getIdToken(),
                oidcUser.getUserInfo());
    }
}

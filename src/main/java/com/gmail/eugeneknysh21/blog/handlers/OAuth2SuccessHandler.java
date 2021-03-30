package com.gmail.eugeneknysh21.blog.handlers;

import com.gmail.eugeneknysh21.blog.providers.JwtTokenProvider;
import com.gmail.eugeneknysh21.blog.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import com.gmail.eugeneknysh21.blog.security.OAuth2UserPrincipal;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken)authentication;
        OAuth2UserPrincipal user = (OAuth2UserPrincipal) token.getPrincipal();
        String accessToken = jwtTokenProvider.createToken(user.getUsername());

        HttpCookieOAuth2AuthorizationRequestRepository.deleteCookies(request, response);
        response.addCookie(jwtTokenProvider.createAccessTokenCookie(accessToken));
        response.sendRedirect("/");
    }
}

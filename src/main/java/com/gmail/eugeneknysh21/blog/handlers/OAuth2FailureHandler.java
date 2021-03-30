package com.gmail.eugeneknysh21.blog.handlers;

import com.gmail.eugeneknysh21.blog.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        HttpCookieOAuth2AuthorizationRequestRepository.deleteCookies(request, response);
        response.sendError(401, "Authentication failed.");
    }
}

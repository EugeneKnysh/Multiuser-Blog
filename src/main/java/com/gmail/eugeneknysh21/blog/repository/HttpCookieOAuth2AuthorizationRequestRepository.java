package com.gmail.eugeneknysh21.blog.repository;

import com.gmail.eugeneknysh21.blog.utility.CookieUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Component
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private static final String AUTHORIZATION_REQUEST_COOKIE_NAME = "CUSTOM_AUTHORIZATION_REQUEST";

    @Value("${oauth2.auth.request.cookies.expiration}")
    private int cookieExpiryMilliseconds;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");

        return CookieUtils.getCookie(request, AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(this::deserialize)
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(response, "response cannot be null");

        if (authorizationRequest == null) {
            deleteCookies(request, response);
        } else {
            Cookie cookie = new Cookie(AUTHORIZATION_REQUEST_COOKIE_NAME, serialize(authorizationRequest));
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(cookieExpiryMilliseconds / 1000);
            response.addCookie(cookie);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return loadAuthorizationRequest(request);
    }

    public static void deleteCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.removeCookie(request, response, AUTHORIZATION_REQUEST_COOKIE_NAME);
    }

    private String serialize(OAuth2AuthorizationRequest authorizationRequest) {

        return Base64.getUrlEncoder().encodeToString(
                SerializationUtils.serialize(authorizationRequest));
    }

    private OAuth2AuthorizationRequest deserialize(Cookie cookie) {

        return (OAuth2AuthorizationRequest) SerializationUtils.deserialize(
                Base64.getUrlDecoder().decode(cookie.getValue()));
    }
}

package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.models.User;
import com.gmail.eugeneknysh21.blog.repository.UserRepository;
import com.gmail.eugeneknysh21.blog.security.OAuth2UserPrincipal;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        return new OAuth2UserPrincipal(
                buildUser(oAuth2User, userRequest),
                oAuth2User.getName(),
                oAuth2User.getAttributes());
    }

    @Transactional
    public User buildUser(OAuth2User oAuth2User, OAuth2UserRequest userRequest) {
        String email = oAuth2User.getAttribute("email");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            Random random = new Random(System.currentTimeMillis());
            String firstName = "";
            String lastName = "";

            if (registrationId.equals("facebook")) {
                firstName = oAuth2User.getAttribute("first_name");
                lastName = oAuth2User.getAttribute("last_name");
            } else if (registrationId.equals("google")) {
                firstName = oAuth2User.getAttribute("given_name");
                lastName = oAuth2User.getAttribute("family_name");
            }

            User newUser = new User(
                    email,
                    "",
                    firstName,
                    lastName,
                    "User" + random.nextInt(999999));
            newUser.setEnabled(true);
            return newUser;
        });

        user.setOauth2id(oAuth2User.getName());
        user.setOauth2Service(registrationId);

        return userRepository.save(user);
    }
}

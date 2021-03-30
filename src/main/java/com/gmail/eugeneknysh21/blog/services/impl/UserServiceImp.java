package com.gmail.eugeneknysh21.blog.services.impl;

import com.gmail.eugeneknysh21.blog.dto.UserDTO;
import com.gmail.eugeneknysh21.blog.dto.UserDataDTO;
import com.gmail.eugeneknysh21.blog.models.User;
import com.gmail.eugeneknysh21.blog.models.VerificationToken;
import com.gmail.eugeneknysh21.blog.repository.UserRepository;
import com.gmail.eugeneknysh21.blog.services.MailSenderService;
import com.gmail.eugeneknysh21.blog.services.UserService;
import com.gmail.eugeneknysh21.blog.services.VerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final MailSenderService mailSenderService;

    @Value("${server.url}")
    private String hostUrl;

    @Override
    @Transactional
    public Long registerUser(UserDTO userDTO) {
        User user = new User(
                userDTO.getEmail(),
                passwordEncoder.encode(userDTO.getPassword()),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getAlias());

        user = userRepository.save(user);

        sendConfirmMessage(user);

        return user.getId();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::getUserDTO)
                .orElseThrow(() -> new EntityNotFoundException("User with such id doesn`t exist."));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::getUserDTO)
                .orElseThrow(() -> new EntityNotFoundException("User with such mail doesn`t exist."));
    }

    @Override
    public Collection<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::getUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                "",
                user.getFirstName(),
                user.getLastName(),
                user.getAlias(),
                user.getRole(),
                user.getStatus(),
                user.isEnabled(),
                user.getOauth2id(),
                user.getOauth2Service()
        );
    }

    @Override
    @Transactional
    public boolean update(UserDataDTO userDataDTO) {
        Long principalId = getPrincipal().getId();
        User user = userRepository.findById(userDataDTO.getId()).orElseThrow(() ->
                new NoSuchElementException("User data can`t be updated. User doesn`t exist."));
        if (user.getId().equals(principalId)) {
            user.setFirstName(userDataDTO.getFirstName());
            user.setLastName(userDataDTO.getLastName());
            user.setAlias(userDataDTO.getAlias());
            user.setEmail(userDataDTO.getEmail());
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean remove(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailIdentity(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAliasIdentity(String alias) {
        return userRepository.findByAlias(alias).isEmpty();
    }

    @Override
    public void sendConfirmMessage(User user) {
        String token = verificationTokenService.createVerificationToken(user);
        String confirmUrl = hostUrl + "/registration/confirm?token=" + token;
        String textMessage = "Dear " + user.getFirstName() +
                ", please follow the link (" + confirmUrl + ") to confirm your registration. " +
                "This link is available within 24 hours.\n" +
                "\n" +
                "This email is generated automatically, please don`t reply to it.";

        mailSenderService.sendMessage(
                user.getEmail(),
                "Registration Confirmation",
                textMessage);
    }

    @Override
    @Transactional
    public boolean confirmRegistration(String token) {
        VerificationToken verificationToken = verificationTokenService.getByToken(token);
        if (verificationTokenService.isExpired(verificationToken)) {
            return false;
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenService.remove(verificationToken);
        return true;
    }

    @Override
    @Transactional
    public boolean resendConfirmLink(String token) {
        VerificationToken verificationToken = verificationTokenService.getByToken(token);
        if (verificationTokenService.isExpired(verificationToken)) {
            User user = verificationToken.getUser();
            verificationTokenService.remove(verificationToken);
            sendConfirmMessage(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return getUserByEmail(userDetails.getUsername());
        }
        throw new NoSuchElementException("User is not logged in.");
    }

    @Override
    public User getPrincipalUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            return userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() ->
                    new NoSuchElementException("User doesn`t exist."));
        }
        throw new NoSuchElementException("User is not logged in.");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkPassword(String password) {
        return passwordEncoder.matches(password, getPrincipalUser().getPassword());
    }

    @Override
    @Transactional
    public boolean updatePassword(Long id, String password) {
        Long principalId = getPrincipal().getId();
        User user = userRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Password can`t be updated. User doesn`t exist."));
        if (user.getId().equals(principalId)) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }
}

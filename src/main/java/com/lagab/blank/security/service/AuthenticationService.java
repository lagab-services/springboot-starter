package com.lagab.blank.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lagab.blank.security.domain.User;
import com.lagab.blank.security.dto.LoginDto;
import com.lagab.blank.security.dto.SignUpDto;
import com.lagab.blank.security.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public User signup(SignUpDto input) {
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.email(),
                        input.password()
                )
        );

        return userRepository.findByEmail(input.email())
                             .orElseThrow();
    }

    //add refresh token

    //TODO: add forgotPassword => creeate a resetpasswordToken available 1hour and send it by mail
    //TODO: add resetPassword => check the resetpasswordToken is valid (text and date), if match we change the password and reset the token to null

}

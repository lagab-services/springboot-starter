package com.lagab.blank.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lagab.blank.common.service.JwtService;
import com.lagab.blank.security.domain.User;
import com.lagab.blank.security.dto.LoginDto;
import com.lagab.blank.security.dto.LoginResponse;
import com.lagab.blank.security.dto.SignUpDto;
import com.lagab.blank.security.service.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthResource {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody SignUpDto signUpDto) {
        User registeredUser = authenticationService.signup(signUpDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginDto loginDto) {
        User authenticatedUser = authenticationService.authenticate(loginDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                                                   .token(jwtToken)
                                                   .expiresIn(jwtService.getExpirationTime())
                                                   .build();

        return ResponseEntity.ok(loginResponse);
    }

}

package com.lagab.blank.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lagab.blank.common.service.JwtService;
import com.lagab.blank.domain.User;
import com.lagab.blank.service.AuthenticationService;
import com.lagab.blank.service.dto.LoginDto;
import com.lagab.blank.service.dto.LoginResponse;
import com.lagab.blank.service.dto.SignUpDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthResource {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody SignUpDto signUpDto) {
        User registeredUser = authenticationService.signup(signUpDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginDto loginDto) {
        User authenticatedUser = authenticationService.authenticate(loginDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = LoginResponse.builder()
                                                   .token(jwtToken)
                                                   .expiresIn(jwtService.getExpirationTime())
                                                   .build();

        return ResponseEntity.ok(loginResponse);
    }

}

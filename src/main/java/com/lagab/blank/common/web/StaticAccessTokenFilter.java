package com.lagab.blank.common.web;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lagab.blank.common.config.CommonProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StaticAccessTokenFilter extends OncePerRequestFilter {
    private final CommonProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("X-Api-Key");

        if (properties.getSecurity().getApiKey().equals(authHeader)) {

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("staticTokenUser", null, List.of(new SimpleGrantedAuthority("ROLE_API_USER"))));

        }

        filterChain.doFilter(request, response);
    }
}

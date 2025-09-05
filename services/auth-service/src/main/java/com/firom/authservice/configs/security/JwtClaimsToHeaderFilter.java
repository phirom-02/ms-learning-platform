package com.firom.authservice.configs.security;

import com.firom.authservice.entRepo.UserRoles;
import com.firom.authservice.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtClaimsToHeaderFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken jwtAuthToken) {
            String token = jwtAuthToken.getToken().getTokenValue();

            String userId = jwtService.extractClaim(token, "userId", String.class);
            String email = jwtService.extractClaim(token, "email", String.class);
            String username = jwtService.getSubject(token);
            List<String> roles = ((List<String>) jwtService.extractClaim(token, "roles", List.class))
                    .stream()
                    .filter(this::isValidRole)
                    .toList();
            request.setAttribute("roles", String.join(",", roles));

            request.setAttribute("userId", userId);
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("roles", String.join(",", roles));
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidRole(String role) {
        try {
            UserRoles.valueOf(role); // throws IllegalArgumentException if invalid
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

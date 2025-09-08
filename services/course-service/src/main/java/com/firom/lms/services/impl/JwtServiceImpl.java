package com.firom.lms.services.impl;

import com.firom.lms.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtDecoder jwtDecoder;

    // Validate token
    @Override
    public boolean isTokenValid(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            Instant now = Instant.now();
            return jwt.getExpiresAt() != null && now.isBefore(jwt.getExpiresAt());
        } catch (JwtException e) {
            return false;
        }
    }

    // Extract all claims
    @Override
    public Map<String, Object> getClaims(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getClaims();
    }

    // Extract any claim using function
    @Override
    public <T> T extractClaim(String token, Function<Jwt, T> claimsResolver) {
        Jwt jwt = jwtDecoder.decode(token);
        return claimsResolver.apply(jwt);
    }

    // Extract claim by name and cast
    @Override
    public <T> T extractClaim(String token, String claimName, Class<T> clazz) {
        Jwt jwt = jwtDecoder.decode(token);
        Object claimValue = jwt.getClaims().get(claimName);
        if (claimValue == null) return null;
        return clazz.cast(claimValue);
    }

    // Extract subject
    @Override
    public String getSubject(String token) {
        return extractClaim(token, Jwt::getSubject);
    }
}

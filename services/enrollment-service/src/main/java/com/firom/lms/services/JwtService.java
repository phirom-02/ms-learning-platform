package com.firom.lms.services;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    boolean isTokenValid(String token);

    Map<String, Object> getClaims(String token);

    <T> T extractClaim(String token, Function<Jwt, T> claimsResolver);

    <T> T extractClaim(String token, String claimName, Class<T> clazz);

    String getSubject(String token);
}

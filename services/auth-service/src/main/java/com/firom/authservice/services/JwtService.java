package com.firom.authservice.services;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {

    RSAKey getRsaKey();

    String generateToken(String subject, Map<String, Object> claims, long ttlSeconds);

    boolean isTokenValid(String token);

    Map<String, Object> getClaims(String token);

    <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver);

    <T> T extractClaim(String token, String claimName, Class<T> clazz);


    String getSubject(String token);

}

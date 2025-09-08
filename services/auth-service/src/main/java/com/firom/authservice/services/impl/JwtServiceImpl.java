package com.firom.authservice.services.impl;

import com.firom.authservice.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {


    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    // Generate token
    @Override
    public String generateToken(String subject, Map<String, Object> customClaims, long ttlSeconds) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(ttlSeconds);


        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(subject)
                .issuedAt(now)
                .expiresAt(expiry)
                .issuer("http://localhost:8083");

        if (customClaims != null) {
            customClaims.forEach(claimsBuilder::claim);
        }

        JwtClaimsSet claims = claimsBuilder.build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

//        try {
//            JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());
//
//            Date now = new Date();
//            Date expiration = new Date(now.getTime() + ttlSeconds * 1000);
//
//
//            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
//                    .subject(subject)
//                    .issuer("http://localhost:8083")
//                    .issueTime(now)
//                    .expirationTime(expiration);
//
//            if (customClaims != null) {
//                customClaims.forEach(claimsSetBuilder::claim);
//            }
//
//            SignedJWT signedJWT = new SignedJWT(
//                    new JWSHeader.Builder(JWSAlgorithm.RS256)
//                            .keyID(rsaKey.getKeyID())
//                            .type(JOSEObjectType.JWT)
//                            .build(),
//                    claimsSetBuilder.build()
//            );
//
//            signedJWT.sign(signer);
//
//            return signedJWT.serialize();
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to generate token", e);
//        }
    }

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

package com.firom.authservice.services.impl;

import com.firom.authservice.services.JwtService;
import com.firom.authservice.utils.RSAKeyUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final RSAKey rsaKey;

    public JwtServiceImpl() {
        this.rsaKey = RSAKeyUtil.loadFromPem();
    }

    @Override
    public RSAKey getRsaKey() {
        return rsaKey;
    }

    // Generate token
    @Override
    public String generateToken(String subject, Map<String, Object> customClaims, long ttlSeconds) {
        try {
            JWSSigner signer = new RSASSASigner(rsaKey.toPrivateKey());

            Date now = new Date();
            Date expiration = new Date(now.getTime() + ttlSeconds * 1000);


            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issuer("http://localhost:8083")
                    .issueTime(now)
                    .expirationTime(expiration);

            if (customClaims != null) {
                customClaims.forEach(claimsSetBuilder::claim);
            }

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID(rsaKey.getKeyID())
                            .type(JOSEObjectType.JWT)
                            .build(),
                    claimsSetBuilder.build()
            );

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate token", e);
        }
    }

    // Validate token
    @Override
    public boolean isTokenValid(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
            return signedJWT.verify(verifier)
                    && new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime());
        } catch (Exception e) {
            return false;
        }
    }

    // Extract all claims
    @Override
    public Map<String, Object> getClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getClaims();
        } catch (ParseException e) {
            throw new RuntimeException("Invalid JWT", e);
        }
    }

    // Extract any claim
    @Override
    public <T> T extractClaim(String token, Function<JWTClaimsSet, T> claimsResolver) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            return claimsResolver.apply(claims);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid JWT", e);
        }
    }

    @Override
    public <T> T extractClaim(String token, String claimName, Class<T> clazz) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            Object claimValue = claims.getClaim(claimName);
            if (claimValue == null) {
                return null;
            }

            return clazz.cast(claimValue);

        } catch (ParseException e) {
            throw new RuntimeException("Invalid JWT token", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Invalid claim type for claim: " + claimName, e);
        }
    }

    @Override
    public String getSubject(String token) {
        return extractClaim(token, JWTClaimsSet::getSubject);
    }

}

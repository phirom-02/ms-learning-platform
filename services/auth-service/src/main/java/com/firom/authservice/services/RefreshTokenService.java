package com.firom.authservice.services;

import com.firom.authservice.configs.security.CustomUserDetails;
import com.firom.authservice.entRepo.RefreshToken;

import java.util.Map;
import java.util.Set;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(CustomUserDetails userDetails, Map<String, Object> claims);

    Set<RefreshToken> getTokensByUsername(String username);

    RefreshToken getTokenByToken(String token);

    void deleteTokensByUsername(String username);

    void deleteByToken(String token);

    boolean isTokenExpired(RefreshToken refreshToken);
}

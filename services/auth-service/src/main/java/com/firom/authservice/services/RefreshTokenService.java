package com.firom.authservice.services;

import com.firom.authservice.entRepo.CustomUserDetails;
import com.firom.authservice.entRepo.RefreshToken;

import java.util.Map;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken(CustomUserDetails userDetails, Map<String, Object> claims);

    RefreshToken getTokenByUsername(String username);

    RefreshToken getTokenByToken(String token);

    void deleteTokenByUsername(String username);

    void deleteByToken(String token);

    boolean isTokenExpired(RefreshToken refreshToken);
}

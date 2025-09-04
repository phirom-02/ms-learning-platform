package com.firom.authservice.services.impl;

import com.firom.authservice.entRepo.CustomUserDetails;
import com.firom.authservice.entRepo.RefreshToken;
import com.firom.authservice.services.JwtService;
import com.firom.authservice.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtService jwtService;
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    @Value("#{new Long('${application.jwt.refresh-token-validity}')}")
    private Long refreshTokenValidity;

    private static final String PREFIX_USERNAME = "refresh:username:";
    private static final String PREFIX_TOKEN = "refresh:token:";

    @Override
    public RefreshToken generateRefreshToken(CustomUserDetails userDetails, Map<String, Object> claims) {
        String generatedToken = jwtService.generateToken(userDetails.getUsername(), claims, refreshTokenValidity);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(generatedToken)
                .userId(userDetails.getUser().getId())
                .username(userDetails.getUsername())
                .refreshTokenExpiresIn(refreshTokenValidity)
                .refreshTokenExpiresAt(new Date().getTime() + refreshTokenValidity)
                .build();

        // Store token → object
        redisTemplate.opsForValue().set(
                PREFIX_TOKEN + refreshToken.getToken(),
                refreshToken,
                refreshTokenValidity,
                TimeUnit.SECONDS
        );

        // Store username → Set<token>
        redisTemplate.opsForSet().add(
                PREFIX_USERNAME + refreshToken.getUsername(),
                refreshToken
        );

        return refreshToken;
    }

    public RefreshToken getTokenByUsername(String username) {
        return redisTemplate.opsForValue().get(PREFIX_USERNAME + username);
    }
}

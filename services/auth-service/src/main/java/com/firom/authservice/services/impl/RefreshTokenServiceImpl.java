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

    @Override
    public RefreshToken getTokenByUsername(String username) {
        return redisTemplate.opsForValue().get(PREFIX_USERNAME + username);
    }

    @Override
    public RefreshToken getTokenByToken(String token) {
        return redisTemplate.opsForValue().get(PREFIX_TOKEN + token);
    }

    @Override
    public void deleteTokenByUsername(String username) {
        RefreshToken token = getTokenByUsername(username);
        if (token != null) {
            redisTemplate.delete(PREFIX_TOKEN + token);
            stringRedisTemplate.delete(PREFIX_TOKEN + token.getToken());
        }
    }

    @Override
    public void deleteByToken(String token) {
        String tokenKey = PREFIX_TOKEN + token;

        RefreshToken refreshToken = redisTemplate.opsForValue().get(tokenKey);
        if (refreshToken != null) {
            String usernameKey = PREFIX_USERNAME + refreshToken.getUsername();

            // Delete the token → object mapping
            redisTemplate.delete(tokenKey);

            // Remove this token from the user's set of tokens
            redisTemplate.opsForSet().remove(usernameKey, refreshToken);

            // delete the username key entirely if no more tokens exist
            Long size = redisTemplate.opsForSet().size(usernameKey);
            if (size != null && size == 0) {
                redisTemplate.delete(usernameKey);
            }
        }
    }

    @Override
    public boolean isTokenExpired(RefreshToken token) {
        return token.getRefreshTokenExpiresAt() < new Date().getTime();
    }
}

package com.firom.authservice.entRepo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    private String token;
    private UUID userId;
    private String username;
    private Long refreshTokenExpiresIn;
    private Long refreshTokenExpiresAt;
}

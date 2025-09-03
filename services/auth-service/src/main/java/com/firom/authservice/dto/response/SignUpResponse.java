package com.firom.authservice.dto.response;

import com.firom.authservice.entRepo.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private Set<UserRoles> roles;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.firom.lms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotEmpty(message = "User name is required")
    private String name;

    @NotEmpty(message = "User email is required")
    @Email(message = "User email is invalid")
    private String email;

//    private String password;
}

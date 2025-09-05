package com.firom.authservice.dto.request;

import com.firom.authservice.validations.PasswordMatches;
import com.firom.authservice.validations.StrongPassword;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class SignUpRequest {

    @NotEmpty(message = "First name is required")
    private String firstName;

    @NotEmpty(message = "Last name is required")
    private String lastName;

    @NotEmpty(message = "Username is mandatory")
    private String username;

    @NotEmpty(message = "Email is required")
    private String email;

    @StrongPassword
    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Password confirm is required")
    private String passwordConfirm;

    @NotNull(message = "User roles is required")
    private List<String> roles;
}

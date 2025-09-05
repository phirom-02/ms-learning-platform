package com.firom.authservice.dto.request;

import com.firom.authservice.validations.PasswordMatches;
import com.firom.authservice.validations.StrongPassword;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatches
public class ChangePasswordRequest {

    @StrongPassword
    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Password confirm is required")
    private String passwordConfirm;
}

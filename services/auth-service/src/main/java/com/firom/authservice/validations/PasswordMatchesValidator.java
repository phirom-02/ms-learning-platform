package com.firom.authservice.validations;

import com.firom.authservice.dto.request.ChangePasswordRequest;
import com.firom.authservice.dto.request.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        String password = "";
        String passwordConfirm = "";

        if (obj instanceof SignUpRequest request) {
            password = request.getPassword();
            passwordConfirm = request.getPasswordConfirm();
        } else if (obj instanceof ChangePasswordRequest request) {
            password = request.getPassword();
            passwordConfirm = request.getPasswordConfirm();
        }

        if (password == null || password.isBlank() || passwordConfirm == null || passwordConfirm.isBlank()) {
            return true;
        }

        boolean matches = password.equals(passwordConfirm);
        if (!matches) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Passwords do not match")
                    .addPropertyNode("passwordConfirm")
                    .addConstraintViolation();
        }

        return matches;
    }
}

package com.firom.authservice.validations;

import com.firom.authservice.dto.request.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, SignUpRequest> {

    @Override
    public boolean isValid(SignUpRequest request, ConstraintValidatorContext context) {
        String password = request.getPassword();
        String passwordConfirm = request.getPasswordConfirm();

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

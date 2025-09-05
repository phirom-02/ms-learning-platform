package com.firom.authservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SignupException extends RuntimeException {
    public SignupException(String message) {
        super(message);
    }
}

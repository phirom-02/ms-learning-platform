package com.firom.authservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LoginException extends RuntimeException {
    public LoginException(String message) {
        super(message);
    }
}

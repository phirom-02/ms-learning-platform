package com.firom.authservice.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountStateException extends RuntimeException {
    public AccountStateException(String message) {
        super(message);
    }
}

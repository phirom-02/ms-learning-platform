package com.firom.lms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CustomDuplicateKeyException extends RuntimeException {
    public CustomDuplicateKeyException(String message) {
        super(message);
    }
}

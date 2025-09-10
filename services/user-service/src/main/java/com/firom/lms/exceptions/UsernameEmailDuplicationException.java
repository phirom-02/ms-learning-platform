package com.firom.lms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class UsernameEmailDuplicationException extends RuntimeException {
    public UsernameEmailDuplicationException(String message) {
        super(message);
    }
}

package com.firom.lms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnrollmentException extends RuntimeException {
    public EnrollmentException(String message) {
        super(message);
    }
}

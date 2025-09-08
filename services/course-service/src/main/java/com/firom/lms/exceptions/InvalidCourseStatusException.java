package com.firom.lms.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidCourseStatusException extends RuntimeException {
    public InvalidCourseStatusException(String message) {
        super(message);
    }
}

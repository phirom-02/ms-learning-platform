package com.firom.lms.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseEnrollmentException extends RuntimeException {
    public CourseEnrollmentException(String message) {
        super(message);
    }
}

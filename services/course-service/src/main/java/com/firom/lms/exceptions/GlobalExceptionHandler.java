package com.firom.lms.exceptions;

import com.firom.lms.dto.response.ErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(MethodArgumentNotValidException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Invalid request body");

        e.getBindingResult().getAllErrors()
                .forEach(err -> {
                    var fieldName = ((FieldError) err).getField();
                    var fieldMessage = err.getDefaultMessage();
                    Map<String, String> errorDetails = new HashMap<>();
                    errorDetails.put(fieldName, fieldMessage);
                    error.put("errorDetails", errorDetails);
                });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(error));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(error));
    }

    @ExceptionHandler(InvalidCourseStatusException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCourseStatusException(InvalidCourseStatusException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(error));
    }
}

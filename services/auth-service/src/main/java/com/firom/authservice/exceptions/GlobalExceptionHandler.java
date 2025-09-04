package com.firom.authservice.exceptions;

import com.firom.authservice.dto.response.ErrorResponse;
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(error));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(UserNotFoundException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(error));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(error));
    }
}

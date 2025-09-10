package com.firom.lms.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firom.lms.dto.response.ErrorResponse;
import feign.FeignException;
import org.springframework.core.annotation.Order;
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

    @Order(0)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Internal server error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(error));
    }

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

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> error = new HashMap<>();
        var status = e.status();

        try {
            String body = e.contentUTF8();
            JsonNode root = mapper.readTree(body);
            String message = root.path("error").path("message").asText();
            error.put("message", message);
        } catch (Exception ex) {
            error.put("message", "Downstream service error");
        }

        return ResponseEntity.status(status).body(new ErrorResponse(error));
    }

    @ExceptionHandler(EnrollmentException.class)
    public ResponseEntity<ErrorResponse> handleCourseEnrollmentException(EnrollmentException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(error));
    }
}


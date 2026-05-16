package com.appsec.lab.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpResponseException.class)
    public ResponseEntity<ErrorResponse> handleHttpResponseException(HttpResponseException ex) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleGeneric(RuntimeException ex) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                500,
                "Internal Error",
                ex.getMessage()
        );
        return ResponseEntity.internalServerError().body(response);
    }
}

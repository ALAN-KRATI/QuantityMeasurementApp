package com.app.quantitymeasurement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {

        return new ResponseEntity<>(new ErrorResponse(
                        LocalDateTime.now(),
                        500,
                        "Internal Server Error",
                        ex.getMessage()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
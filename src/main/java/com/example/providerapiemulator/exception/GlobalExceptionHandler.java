package com.example.providerapiemulator.exception;

import com.example.providerapiemulator.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<GenericResponse<Void>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<GenericResponse<Void>> handleBadRequest(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<GenericResponse<Void>> handleMissing(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GenericResponse.fail(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Void>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GenericResponse.fail("Unexpected error: " + ex.getMessage()));
    }
}

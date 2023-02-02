package com.gmi.gameInfo.exceptionHandler;

import com.gmi.gameInfo.member.exception.DifferentAuthEmailNumberException;
import com.gmi.gameInfo.member.exception.NotFoundAuthEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DifferentAuthEmailNumberException.class)
    protected ResponseEntity<?> handleDifferentAuthEmailNumberException(DifferentAuthEmailNumberException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundAuthEmailException.class)
    protected ResponseEntity<?> handleNotFoundAuthEmailException(NotFoundAuthEmailException e) {
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

package com.gmi.gameInfo.exceptionHandler;

import com.gmi.gameInfo.member.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DifferentAuthEmailNumberException.class)
    protected ResponseEntity<?> handleDifferentAuthEmailNumberException(DifferentAuthEmailNumberException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundAuthEmailException.class)
    protected ResponseEntity<?> handleNotFoundAuthEmailException(NotFoundAuthEmailException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(SendEmailFailException.class)
    protected ResponseEntity<?> handleSendEmailFailException(SendEmailFailException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundMemberException.class)
    protected ResponseEntity<?> handleNotFoundMemberException(NotFoundMemberException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    protected ResponseEntity<?> handleDuplicateEmailException(DuplicateEmailException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(DuplicateMemberException.class)
    protected ResponseEntity<?> handleDuplicateMemberException(DuplicateMemberException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

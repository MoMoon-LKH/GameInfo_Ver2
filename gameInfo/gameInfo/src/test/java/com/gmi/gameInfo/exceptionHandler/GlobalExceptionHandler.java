package com.gmi.gameInfo.exceptionHandler;

import com.gmi.gameInfo.category.exception.NotFoundCategoryException;
import com.gmi.gameInfo.comment.exception.NotFoundCommentException;
import com.gmi.gameInfo.exceptionHandler.exception.NoPermissionException;
import com.gmi.gameInfo.games.exception.NotFoundGameException;
import com.gmi.gameInfo.games.exception.GameSameNameException;
import com.gmi.gameInfo.image.exception.FailDeleteFileException;
import com.gmi.gameInfo.image.exception.FailUploadFileException;
import com.gmi.gameInfo.image.exception.NotFoundFileException;
import com.gmi.gameInfo.image.exception.NotFoundImagesException;
import com.gmi.gameInfo.member.exception.*;
import com.gmi.gameInfo.news.exception.NotFoundNewsException;
import com.gmi.gameInfo.platform.exception.NotFoundPlatformException;
import com.gmi.gameInfo.post.exception.FailDeletePostException;
import com.gmi.gameInfo.post.exception.NotFoundPostException;
import com.gmi.gameInfo.post.exception.NotPostOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    @ExceptionHandler(DuplicateMemberIdException.class)
    protected ResponseEntity<?> handleDuplicateMemberIdException(DuplicateMemberIdException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(LoginFailException.class)
    protected ResponseEntity<?> handleLoginFailException(LoginFailException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotFoundPostException.class)
    protected ResponseEntity<?> handleNotFoundPostException(NotFoundPostException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FailDeletePostException.class)
    protected ResponseEntity<?> handleFailDeletePostException(FailDeletePostException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage()).build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NotPostOwnerException.class)
    protected ResponseEntity<?> handleNotPostOwnerException(NotPostOwnerException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage()).build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NotFoundCategoryException.class)
    protected ResponseEntity<?> handleNotFoundCategoryException(NotFoundCategoryException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(NotFoundImagesException.class)
    protected ResponseEntity<?> handleNotFoundImagesException(NotFoundImagesException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FailUploadFileException.class)
    protected ResponseEntity<?> handleFailUploadFileException(FailUploadFileException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundFileException.class)
    protected ResponseEntity<?> handleNotFoundFileException(NotFoundFileException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FailDeleteFileException.class)
    protected ResponseEntity<?> handleFailDeleteFileException(FailDeleteFileException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundPlatformException.class)
    protected ResponseEntity<?> handleNotFoundPlatformException(NotFoundPlatformException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NotFoundNewsException.class)
    protected ResponseEntity<?> handleNotFoundNewsException(NotFoundNewsException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoPermissionException.class)
    protected ResponseEntity<?> handleNoPermissionException(NoPermissionException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(NotFoundCommentException.class)
    protected ResponseEntity<?> handleNotFoundCommentException(NotFoundCommentException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NotFoundGameException.class)
    protected ResponseEntity<?> handleNotFoundGameException(NotFoundGameException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(GameSameNameException.class)
    protected ResponseEntity<?> handleGameSameNameException(GameSameNameException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    protected ResponseEntity<?> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message("다시 시도해주세요")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

}

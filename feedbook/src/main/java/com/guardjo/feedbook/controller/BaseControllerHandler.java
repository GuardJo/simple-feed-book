package com.guardjo.feedbook.controller;

import com.guardjo.feedbook.controller.response.BaseResponse;
import com.guardjo.feedbook.exception.DuplicateUsernameException;
import com.guardjo.feedbook.exception.EntityNotFoundException;
import com.guardjo.feedbook.exception.InvalidRequestException;
import com.guardjo.feedbook.exception.WrongPasswordException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackageClasses = {
        AccountController.class,
        FeedController.class
})
@Slf4j
public class BaseControllerHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {
            IllegalArgumentException.class
    })
    public ResponseEntity<BaseResponse<String>> handleBadRequest(Exception e) {
        log.warn("Exception : BadRequest, ", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                BaseResponse.<String>builder()
                        .body(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST.name())
                        .build()
        );
    }

    @ExceptionHandler(value = {
            WrongPasswordException.class
    })
    public ResponseEntity<BaseResponse<String>> handleUnauthorized(Exception e) {
        log.warn("Exception : Unauthorize, ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                BaseResponse.<String>builder()
                        .body(e.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.name())
                        .build()
        );
    }

    @ExceptionHandler(value = {
            InvalidRequestException.class
    })
    public ResponseEntity<BaseResponse<String>> handleForbidden(Exception e) {
        log.warn("Exception : Forbidden, ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                BaseResponse.<String>builder()
                        .body(e.getMessage())
                        .status(HttpStatus.FORBIDDEN.name())
                        .build()
        );
    }

    @ExceptionHandler(value = {
            EntityNotFoundException.class
    })
    public ResponseEntity<BaseResponse<String>> handleNotFound(Exception e) {
        log.warn("Exception : NotFound, ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                BaseResponse.<String>builder()
                        .body(e.getMessage())
                        .status(HttpStatus.NOT_FOUND.name())
                        .build()
        );
    }

    @ExceptionHandler(value = {
            DuplicateUsernameException.class
    })
    public ResponseEntity<BaseResponse<String>> handleConflict(Exception e) {
        log.warn("Exception : Conflict, ", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                BaseResponse.<String>builder()
                        .body(e.getMessage())
                        .status(HttpStatus.CONFLICT.name())
                        .build()
        );
    }
}

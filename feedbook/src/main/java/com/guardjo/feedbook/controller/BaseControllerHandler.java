package com.guardjo.feedbook.controller;

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
    public ResponseEntity<String> handleBadRequest(Exception e) {
        log.warn("Exception : BadRequest, ", e);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(value = {
            WrongPasswordException.class
    })
    public ResponseEntity<String> handleUnauthorized(Exception e) {
        log.warn("Exception : Unauthorize, ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(value = {
            InvalidRequestException.class
    })
    public ResponseEntity<String> handleForbidden(Exception e) {
        log.warn("Exception : Forbidden, ", e);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(value = {
            EntityNotFoundException.class
    })
    public ResponseEntity<String> handleNotFound(Exception e) {
        log.warn("Exception : NotFound, ", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(value = {
            DuplicateUsernameException.class
    })
    public ResponseEntity<String> handleConflict(Exception e) {
        log.warn("Exception : Conflict, ", e);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}

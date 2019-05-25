package com.rps;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.rps.exception.DeletedGameException;
import com.rps.exception.FinishedGameException;
import com.rps.exception.GameDoesNotExistsException;

@ControllerAdvice
public class ExceptionHandlerConfiguration {
    private ResponseEntity<ErrorResponse> handleException(WebRequest request, Exception exception, HttpStatus status) {
        ErrorResponse response = new ErrorResponse(status, exception.getMessage(), request.getContextPath());
        return new ResponseEntity<ErrorResponse>(response, status);
    }

    @ExceptionHandler(GameDoesNotExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(WebRequest request, GameDoesNotExistsException exception) {
        return handleException(request, exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        DeletedGameException.class,
        FinishedGameException.class
    })
    public ResponseEntity<ErrorResponse> handleException(WebRequest request, Exception exception) {
        return handleException(request, exception, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleSystemExceptions(WebRequest request, GameDoesNotExistsException exception) {
        return handleException(request, exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleException(WebRequest request, IllegalArgumentException exception) {
        return handleException(request, exception, HttpStatus.NOT_FOUND);
    }
}

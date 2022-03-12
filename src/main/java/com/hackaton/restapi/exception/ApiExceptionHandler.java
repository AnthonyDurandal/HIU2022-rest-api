package com.hackaton.restapi.exception;


import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;

import com.hackaton.restapi.entity.error.ErrorDetails;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = { ApiRequestException.class })
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e) {
        ErrorDetails errorDetails = new ErrorDetails(
                false,
                e.getMessage(),
                new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorDetails errorDetails = new ErrorDetails(
                false,
                "Le chemin spécifié n'a pas été trouvé sur ce serveur",
                new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(errorDetails, notFound);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e) {
        HttpStatus notFound = HttpStatus.BAD_REQUEST;
        ErrorDetails errorDetails = new ErrorDetails(
                false,
                e.getMessage(),
                new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(errorDetails, notFound);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Object> handleDisabledException(DisabledException e) {
        HttpStatus badReq = HttpStatus.BAD_REQUEST;
        ErrorDetails errorDetails = new ErrorDetails(
                false,
                e.getMessage(),
                new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(errorDetails, badReq);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        HttpStatus badReq = HttpStatus.BAD_REQUEST;
        ErrorDetails errorDetails = new ErrorDetails(
                false,
                e.getMessage(),
                new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(errorDetails, badReq);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException e) {
        HttpStatus forb = HttpStatus.FORBIDDEN;
        ErrorDetails errorDetails = new ErrorDetails(
                false,
                e.getMessage(),
                new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(errorDetails, forb);
    }
}
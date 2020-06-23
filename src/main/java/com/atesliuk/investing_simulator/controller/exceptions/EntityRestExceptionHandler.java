package com.atesliuk.investing_simulator.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * A custom exception handler
 */
@ControllerAdvice
public class EntityRestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<EntityErrorResponse> handleException(EntityNotFoundException exc){

        EntityErrorResponse error = new EntityErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        //error - goes to the body of HTTP response; Status - goes to the header of the HTTP response
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //catches all exceptions
    @ExceptionHandler
    public ResponseEntity<EntityErrorResponse> handleException(Exception exc){

        EntityErrorResponse error = new EntityErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                exc.getMessage(),
                System.currentTimeMillis());

        //error - goes to the body of HTTP response; Status - goes to the header of the HTTP response
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}

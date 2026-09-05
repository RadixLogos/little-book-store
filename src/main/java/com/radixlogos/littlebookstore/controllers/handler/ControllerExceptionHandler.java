package com.radixlogos.littlebookstore.controllers.handler;

import com.radixlogos.littlebookstore.dto.error.CustomError;
import com.radixlogos.littlebookstore.dto.error.ValidationError;
import com.radixlogos.littlebookstore.services.exceptions.DatabaseException;
import com.radixlogos.littlebookstore.services.exceptions.ForbiddenException;
import com.radixlogos.littlebookstore.services.exceptions.ResourceNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.NOT_FOUND;
        CustomError err = new CustomError(Instant.now(),status.value(),e.getMessage(),request.getRequestURI());
        return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> dataBase(DatabaseException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError err = new ValidationError(Instant.now(),status.value(),"Dados inválidos",request.getRequestURI());
        for(FieldError f : e.getBindingResult().getFieldErrors()) {
            err.addErrors(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<CustomError> forbidden(ForbiddenException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.FORBIDDEN;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status.value()).body(err);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CustomError> expired(ExpiredJwtException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status.value()).body(err);
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CustomError> expired(NoSuchElementException e, HttpServletRequest request){
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        CustomError err = new CustomError(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status.value()).body(err);
    }

}

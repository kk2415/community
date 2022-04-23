package com.together.levelup.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAllExceptions(Exception e, HttpServletRequest request) {
//        ExceptionResponse exceptionResponse = new ExceptionResponse();
//
//        exceptionResponse.setTimeStamp(LocalDateTime.now());
//        exceptionResponse.setMessage(e.getMessage());
//        exceptionResponse.setException(e.getClass().getName());
//        exceptionResponse.setPath(request.getRequestURI());
//
//        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(MemberNotFoundException.class)
    public final ResponseEntity<Object> memberNotFoundException(MemberNotFoundException e, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        System.out.println(e.getMessage());
        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(e.getClass().getName());
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ExceptionResponse> duplicateEmailExceptionHandler(Exception e, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(e.getClass().getName());
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                                  HttpServletRequest request) {

        CreateExceptionResponse createExceptionResponse = new CreateExceptionResponse();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        for (FieldError fieldError : fieldErrors) {
            System.out.println(fieldError.getDefaultMessage());
            createExceptionResponse.message.put(
                    fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        createExceptionResponse.setTimeStamp(LocalDateTime.now());
        createExceptionResponse.setException(e.getClass().getName());
        createExceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(createExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundImageException.class)
    public ResponseEntity<ExceptionResponse> notFoundImageException(Exception e, HttpServletRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();

        exceptionResponse.setTimeStamp(LocalDateTime.now());
        exceptionResponse.setMessage(e.getMessage());
        exceptionResponse.setException(e.getClass().getName());
        exceptionResponse.setPath(request.getRequestURI());

        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}

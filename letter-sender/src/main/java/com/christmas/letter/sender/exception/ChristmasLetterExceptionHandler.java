package com.christmas.letter.sender.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ChristmasLetterExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException ex){

        List<String> errors = ex.getAllErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.badRequest().body((new ErrorResponse("The validation has failed", errors)));
    }
}

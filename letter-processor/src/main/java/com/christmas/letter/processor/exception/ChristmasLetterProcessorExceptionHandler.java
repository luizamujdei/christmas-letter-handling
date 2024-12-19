package com.christmas.letter.processor.exception;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@ControllerAdvice
public class ChristmasLetterProcessorExceptionHandler {

    @ExceptionHandler({HandlerMethodValidationException.class})
    public ResponseEntity<ErrorResponse> methodValidationExceptionHandler(HandlerMethodValidationException e){
        List<String> errors = e.getAllErrors().stream().map(MessageSourceResolvable::getDefaultMessage).toList();

        return ResponseEntity.badRequest().body((new ErrorResponse("The validation has failed", errors)));
    }

}

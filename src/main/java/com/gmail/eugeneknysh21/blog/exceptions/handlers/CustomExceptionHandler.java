package com.gmail.eugeneknysh21.blog.exceptions.handlers;

import com.gmail.eugeneknysh21.blog.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMessage = "";
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorMessage += fieldError.getDefaultMessage() + " \n";
        }
        return ResponseEntity.badRequest().body(new ResponseDTO(4001, errorMessage));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ResponseDTO> handleNoSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.badRequest().body(new ResponseDTO(4002, exception.getMessage()));
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ResponseDTO> handleMailException() {
        return ResponseEntity.badRequest().body(new ResponseDTO(4003, "Error sending message."));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDTO(4041, exception.getMessage()));
    }
}

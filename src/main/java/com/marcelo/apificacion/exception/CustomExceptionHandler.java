package com.marcelo.apificacion.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /*
    * Metodo que maneja las excepciones especificas de tipo RecordNotFoundException tiradas en el RestController
    * */
    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorResponse> handleRecordNotFound(RecordNotFoundException ex) {
        return new ResponseEntity(new ErrorResponse("g100", ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    /*
     * Metodo que maneja las excepciones especificas de tipo BadRequestException tiradas en el RestController
     * */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity(new ErrorResponse("g101", "Parámetros inválidos"),
                HttpStatus.BAD_REQUEST);
    }

    /*
     * Metodo que maneja otro tipo de excepciones tiradas en el RestController
     * */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity(new ErrorResponse("g102", "Error interno del servidor"),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
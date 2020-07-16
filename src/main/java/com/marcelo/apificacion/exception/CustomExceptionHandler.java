package com.marcelo.apificacion.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /*
    * Metodo que maneja las excepciones especificas de tipo RecordNotFoundException tiradas en el RestController
    * */
    @ExceptionHandler(RecordNotFoundException.class)
    protected ResponseEntity<Object> handleRecordNotFound(RecordNotFoundException ex) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("codigo", "g100");
        objectNode.put("error", ex.getMessage());

        return new ResponseEntity(objectNode, HttpStatus.NOT_FOUND);
    }

    /*
     * Metodo que maneja las excepciones especificas de tipo BadRequestException tiradas en el RestController
     * */
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("codigo", "g101");
        objectNode.put("error", "Parámetros inválidos");

        return new ResponseEntity(objectNode, HttpStatus.BAD_REQUEST);
    }

    /*
     * Metodo que maneja otro tipo de excepciones tiradas en el RestController
     * */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ObjectNode> handleAllOtherExceptions(Exception ex, WebRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("codigo", "g102");
        objectNode.put("error", "Error interno del servidor");

        return new ResponseEntity(objectNode, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
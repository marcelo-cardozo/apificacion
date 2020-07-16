package com.marcelo.apificacion.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marcelo.apificacion.exception.BadRequestException;
import com.marcelo.apificacion.exception.RecordNotFoundException;
import com.marcelo.apificacion.service.IAseguradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/consulta")
@CrossOrigin("*")
public class ConsultaController {

    private IAseguradoService aseguradoService;

    public ConsultaController (IAseguradoService aseguradoService){
        this.aseguradoService = aseguradoService;
    }

    @GetMapping(
            value = "/{cedula}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<ObjectNode> consultaAsegurado(@PathVariable(value = "cedula") String cedula) {
        // Si la cedula contiene algun caracter que no sea un numero, tirar un BadRequest
        if (!cedula.matches("[0-9]*")) {
            throw new BadRequestException();
        }

        // Si se obtiene exitosamente los datos del asegurado, retornar los mismos, si no, tirar un RecordNotFound
        Optional<ObjectNode> asegurado = aseguradoService.findAseguradoByCi(cedula);
        if (asegurado.isPresent()) {
            return new ResponseEntity<>(asegurado.get(), HttpStatus.OK);
        } else {
            throw new RecordNotFoundException(String.format("usuario con cédula %s no existe", cedula));
        }
    }

}

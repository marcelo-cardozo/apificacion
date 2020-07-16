package com.marcelo.apificacion.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marcelo.apificacion.exception.BadRequestException;
import com.marcelo.apificacion.exception.ErrorResponse;
import com.marcelo.apificacion.exception.RecordNotFoundException;
import com.marcelo.apificacion.service.IAseguradoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private static final String cedulaPattern = "[0-9]*";

    private IAseguradoService aseguradoService;

    public ConsultaController(IAseguradoService aseguradoService) {
        this.aseguradoService = aseguradoService;
    }

    @Operation(summary = "Obtiene los datos de una persona que posee IPS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La persona posee IPS",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Formato incorrecto de la cédula",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Persona no posee IPS",
                    content = @Content)})
    @GetMapping(
            value = "/{cedula}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ObjectNode consultaAsegurado(
            @Parameter(description = "Cédula de la persona",
                    example = "1234567",
                    content = @Content(schema = @Schema(pattern = cedulaPattern)))
            @PathVariable(value = "cedula") String cedula) {
        // Si la cedula contiene algun caracter que no sea un numero, tirar un BadRequest
        if (!cedula.matches(cedulaPattern)) {
            throw new BadRequestException();
        }

        // Si se obtiene exitosamente los datos del asegurado, retornar los mismos, si no, tirar un RecordNotFound
        Optional<ObjectNode> asegurado = aseguradoService.findAseguradoByCi(cedula);
        if (asegurado.isPresent()) {
            return asegurado.get();
        } else {
            throw new RecordNotFoundException(String.format("usuario con cédula %s no existe", cedula));
        }
    }

}

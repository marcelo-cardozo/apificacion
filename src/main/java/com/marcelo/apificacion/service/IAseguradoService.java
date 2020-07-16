package com.marcelo.apificacion.service;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Optional;

public interface IAseguradoService {
    Optional<ObjectNode> findAseguradoByCi(String ci);
}

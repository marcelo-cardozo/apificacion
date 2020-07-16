package com.marcelo.apificacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marcelo.apificacion.service.IAseguradoService;
import com.marcelo.apificacion.service.impl.AseguradoServiceImpl;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AseguradoServiceTests {

    RestTemplate restTemplate = mock(RestTemplate.class);
    ObjectMapper mapper = new ObjectMapper();

    AseguradoServiceImpl aseguradoService = new AseguradoServiceImpl(restTemplate, mapper);

    @SneakyThrows
    @Test
    public void test_aseguradoTieneIps() {
        String okHtml = FileUtils.readTestResourceFile("testdata/ok.html");
        String expected = FileUtils.readTestResourceFile("testdata/ok_result.json");

        when(restTemplate.postForEntity(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(new ResponseEntity(okHtml, HttpStatus.OK));

        Optional<ObjectNode> objectNode = aseguradoService.findAseguradoByCi("1234567");
        ObjectNode expectedNode = mapper.readValue(expected, ObjectNode.class);
        assert expectedNode.equals(objectNode.get());
    }

    @SneakyThrows
    @Test
    public void test_aseguradoNoTieneIps() {
        String noHtml = FileUtils.readTestResourceFile("testdata/no.html");

        when(restTemplate.postForEntity(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(new ResponseEntity(noHtml, HttpStatus.OK));

        Optional<ObjectNode> objectNode = aseguradoService.findAseguradoByCi("1");

        assert objectNode.isEmpty();
    }
}

package com.marcelo.apificacion;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={ ApificacionApplication.class })
public class ConsultaControllerTests {
    @MockBean
    RestTemplate restTemplate;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    public void test_aseguradoTieneIps() {
        String okHtml = FileUtils.readTestResourceFile("testdata/ok.html");
        String expected = FileUtils.readTestResourceFile("testdata/result_ok.json");
        ObjectNode expectedNode = mapper.readValue(expected, ObjectNode.class);


        when(restTemplate.postForEntity(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(new ResponseEntity(okHtml, HttpStatus.OK));

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/consulta/1234567")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = resultActions.andReturn();
        String resultContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectNode resultNode = mapper.readValue(resultContent, ObjectNode.class);

        assert expectedNode.equals(resultNode);
    }

    @SneakyThrows
    @Test
    public void test_aseguradoNoTieneIps() {
        String html = FileUtils.readTestResourceFile("testdata/no.html");
        String expected = FileUtils.readTestResourceFile("testdata/result_no.json");
        ObjectNode expectedNode = mapper.readValue(expected, ObjectNode.class);


        when(restTemplate.postForEntity(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(new ResponseEntity(html, HttpStatus.OK));

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/consulta/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = resultActions.andReturn();
        String resultContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectNode resultNode = mapper.readValue(resultContent, ObjectNode.class);

        assert expectedNode.equals(resultNode);
    }

    @SneakyThrows
    @Test
    public void test_badRequest() {
        String html = FileUtils.readTestResourceFile("testdata/no.html");
        String expected = FileUtils.readTestResourceFile("testdata/result_bad_request.json");
        ObjectNode expectedNode = mapper.readValue(expected, ObjectNode.class);

        when(restTemplate.postForEntity(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(new ResponseEntity(html, HttpStatus.OK));

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/consulta/aa")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = resultActions.andReturn();
        String resultContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectNode resultNode = mapper.readValue(resultContent, ObjectNode.class);

        assert expectedNode.equals(resultNode);
    }

    @SneakyThrows
    @Test
    public void test_serverError() {
        String html = FileUtils.readTestResourceFile("testdata/no.html");
        String expected = FileUtils.readTestResourceFile("testdata/result_server_error.json");
        ObjectNode expectedNode = mapper.readValue(expected, ObjectNode.class);

        when(restTemplate.postForEntity(any(String.class), any(Object.class), any(Class.class)))
                .thenReturn(null);

        ResultActions resultActions = mockMvc
                .perform(MockMvcRequestBuilders.get("/consulta/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        MvcResult result = resultActions.andReturn();
        String resultContent = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ObjectNode resultNode = mapper.readValue(resultContent, ObjectNode.class);

        assert expectedNode.equals(resultNode);
    }
}

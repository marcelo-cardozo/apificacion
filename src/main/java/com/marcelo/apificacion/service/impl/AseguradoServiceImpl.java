package com.marcelo.apificacion.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.marcelo.apificacion.service.IAseguradoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class AseguradoServiceImpl implements IAseguradoService {

    private String consultaAseguradoUrl = "https://servicios.ips.gov.py/consulta_asegurado/comprobacion_de_derecho_externo.php";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public Optional<ObjectNode> findAseguradoByCi(String ci) {

        String htmlResponse = getHtmlResponseFromIps(ci);

        return parseHtmlData(htmlResponse);
    }

    /*
    * Dado la cedula del asegurado, retornar el HTML generado por la pagina del IPS
    * */
    private String getHtmlResponseFromIps(String ci) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("nro_cic", ci);
        map.add("recuperar", "Recuperar");
        map.add("envio", "ok");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(consultaAseguradoUrl, request, String.class);

        return response.getBody();
    }

    /*
    * Parsear el html de la pagina y retornar los datos en caso de que el asegurado posea IPS
    * */
    private Optional<ObjectNode> parseHtmlData(String htmlResponse) {

        Document doc = Jsoup.parse(htmlResponse);
        Elements tables = doc.select("table");

        // La tercera (2) tabla dentro del HTML posee los datos de la persona
        Element personaTable = tables.get(2);

        // si no se encuentra una persona asegurada con el numero de cedula, retornar vacio
        // se hace la comparacion con 1, debido a que el primer tr es el header y los siguientes son los datos
        if (personaTable.select("tr").size() <= 1)
            return Optional.empty();

        ObjectNode resultObject = mapper.createObjectNode();

        ObjectNode personaData = getTableData(personaTable, 1);
        resultObject.set("persona", personaData);

        // La cuarta (3) tabla dentro del HTML posee los datos del empleador
        Element empleadorTable = tables.get(3);
        ObjectNode empleadorData = getTableData(empleadorTable, 0);
        resultObject.set("empleador", empleadorData);

        return Optional.of(resultObject);
    }

    /*
    * Dado una tabla HTML cargar el titulo (header) como key y el valor de la primera fila de la tabla como value
    * dentro del response
    * */
    private ObjectNode getTableData(Element table, int columnStart) {
        ObjectNode objectNode = mapper.createObjectNode();

        Elements filas = table.select("tr");

        if (filas.size() > 1) {
            Element filaTitulos = filas.get(0);
            Elements titulos = filaTitulos.select("th");

            Element filaDatos = filas.get(1);
            Elements datos = filaDatos.select("td");

            for (int i = columnStart; i < titulos.size() && i < datos.size(); i++) {
                String titulo = titulos.get(i).text().replace(' ','_');
                String dato = datos.get(i).text();

                objectNode.put(titulo, dato);
            }
        }

        return objectNode;
    }
}

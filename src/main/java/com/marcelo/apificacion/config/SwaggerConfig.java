package com.marcelo.apificacion.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.marcelo.apificacion"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Apificacion",
                "El Rest API posee un endpoint relacionado a la obtención de datos de una persona asegurada en el Instituto de Previsión Social.",
                "V1",
                null,
                new Contact("Marcelo Cardozo", "http://www.github.com/marcelo-cardozo", "marcelo.r.cardozo.g@gmail.com"),
                null, null, Collections.emptyList());
    }
}

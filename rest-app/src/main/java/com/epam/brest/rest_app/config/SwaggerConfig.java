package com.epam.brest.rest_app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket productApi()
    {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .apiInfo(metaInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.epam.brest"))
                .paths(regex("/.*"))
                .build().apiInfo(metaInfo());
    }

    private ApiInfo metaInfo() {

        return new ApiInfo(
                "Swagger Documentation",
                "Swagger for Project Employee",
                "1.0",
                "Terms of Service",
                ApiInfo.DEFAULT_CONTACT,
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html",
                new ArrayList<>());
    }

}

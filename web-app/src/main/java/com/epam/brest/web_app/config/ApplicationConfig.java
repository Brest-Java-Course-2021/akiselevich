package com.epam.brest.web_app.config;

import com.epam.brest.service.*;
import com.epam.brest.service.rest.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
public class ApplicationConfig {

    @Value("${rest.server.protocol}")
    private String protocol;

    @Value("${rest.server.host}")
    private String host;

    @Value("${rest.server.port}")
    private Integer port;

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    ProjectService projectService() {
        String url = String.format("%s://%s:%d/projects", protocol, host, port);
        return new ProjectServiceRest(url, restTemplate());
    }

    @Bean
    ProjectDTOService projectDTOService() {
        String url = String.format("%s://%s:%d/project-dtos", protocol, host, port);
        return new ProjectDTOServiceRest(url, restTemplate());
    }

    @Bean
    EmployeeService employeeService() {
        String url = String.format("%s://%s:%d/employees", protocol, host, port);
        return new EmployeeServiceRest(url, restTemplate());
    }

    @Bean
    EmployeeDTOService employeeDTOService() {
        String url = String.format("%s://%s:%d/employee-dtos", protocol, host, port);
        return new EmployeeDTOServiceRest(url, restTemplate());
    }

    @Bean
    RoleService roleService() {
        String url = String.format("%s://%s:%d/roles", protocol, host, port);
        return new RoleServiceRest(url, restTemplate());
    }

}

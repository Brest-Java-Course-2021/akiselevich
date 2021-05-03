package com.epam.brest.service.rest.config;

import com.epam.brest.service.*;
import com.epam.brest.service.rest.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TestConfig {

    public static final String PROJECTS_DTOS_URL = "http://localhost:8088/projects-dtos";
    public static final String PROJECTS_URL = "http://localhost:8088/projects";
    public static final String EMPLOYEES_DTOS_URL = "http://localhost:8088/employees-dtos";
    public static final String EMPLOYEES_URL = "http://localhost:8088/employees";
    public static final String ROLES_URL = "http://localhost:8088/roles";

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }

    @Bean
    ProjectService projectService() {
        return new ProjectServiceRest(PROJECTS_URL, restTemplate());
    }

    @Bean
    ProjectDTOService projectDTOService() {
        return new ProjectDTOServiceRest(PROJECTS_DTOS_URL, restTemplate());
    }

    @Bean
    EmployeeService employeeService() {
        return new EmployeeServiceRest(EMPLOYEES_URL, restTemplate());
    }

    @Bean
    EmployeeDTOService employeeDTOService() {
        return new EmployeeDTOServiceRest(EMPLOYEES_DTOS_URL, restTemplate());
    }

    @Bean
    RoleService roleService() {
        return new RoleServiceRest(ROLES_URL, restTemplate());
    }

}

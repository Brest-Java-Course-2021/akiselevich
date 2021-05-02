package com.epam.brest.service.rest;

import com.epam.brest.model.Employee;
import com.epam.brest.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceRest implements EmployeeService {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceRest.class);

    private final String url;

    private final RestTemplate restTemplate;

    public EmployeeServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Employee> findAll() {
        LOGGER.debug("Rest service method called to find all Employee");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Employee>) responseEntity.getBody();
    }

    @Override
    public Optional<Employee> findById(Integer employeeId) {
        LOGGER.debug("Rest service method called to find Employee by Id: {}", employeeId);
        ResponseEntity<Employee> responseEntity =
                restTemplate.getForEntity(url + "/" + employeeId, Employee.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<Employee> create(Employee employee) {
        LOGGER.debug("Rest service method called to create Employee: {}", employee);
        ResponseEntity<Employee> responseEntity = restTemplate.postForEntity(url, employee, Employee.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<Employee> update(Employee employee) {
        LOGGER.debug("Rest service method called to update Employee with id: {}, new Employee: {}", employee.getEmployeeId(), employee);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Employee> entity = new HttpEntity<>(employee, headers);
        ResponseEntity<Employee> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, Employee.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void delete(Integer employeeId) {
        LOGGER.debug("Rest service method called to delete Employee by Id: {}", employeeId);
        restTemplate.delete(url + "/" + employeeId);
    }

}

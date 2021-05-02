package com.epam.brest.service.rest;

import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeDTOServiceRest implements EmployeeDTOService {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeDTOServiceRest.class);

    private final String url;

    private final RestTemplate restTemplate;

    public EmployeeDTOServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        LOGGER.debug("Rest service method called to find all Employee");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<EmployeeDTO>) responseEntity.getBody();
    }

    @Override
    public Optional<EmployeeDTO> findById(Integer employeeId) {
        LOGGER.debug("Rest service method called to find Employee by Id: {}", employeeId);
        ResponseEntity<EmployeeDTO> responseEntity =
                restTemplate.getForEntity(url + "/" + employeeId, EmployeeDTO.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

}

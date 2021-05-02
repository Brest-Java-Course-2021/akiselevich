package com.epam.brest.rest_app;

import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class EmployeeDTOController {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeDTOController.class);

    private final EmployeeDTOService employeeDTOService;

    public EmployeeDTOController(EmployeeDTOService employeeDTOService) {
        this.employeeDTOService = employeeDTOService;
    }

    @GetMapping(value = "/employee-dtos")
    public Collection<EmployeeDTO> employeeDtos() {
        LOGGER.debug("Controller method called to view all Employees");
        return employeeDTOService.findAll();
    }

    @GetMapping(value = "/employee-dtos/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeDtoById(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to view Employee by Id: {}", id);
        Optional<EmployeeDTO> optional = employeeDTOService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

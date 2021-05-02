package com.epam.brest.rest_app;

import com.epam.brest.model.Employee;
import com.epam.brest.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class EmployeeController {

    private static final Logger LOGGER = LogManager.getLogger(ProjectController.class);

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/employees")
    public Collection<Employee> employees() {
        LOGGER.debug("Controller method called to view all Employees");
        return employeeService.findAll();
    }

    @GetMapping(value = "/employees/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to view Employee by Id: {}", id);
        Optional<Employee> optional = employeeService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        LOGGER.debug("Controller method called to create new Employee: {}", employee);
        return new ResponseEntity<>(employeeService.create(employee).get(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/employees", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee) {
        LOGGER.debug("Controller method called to update Employee: {}", employee);
        return new ResponseEntity<>(employeeService.update(employee).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/employees/{id}")
    public HttpStatus deleteEmployee(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to delete Employee with Id: {}", id);
        employeeService.delete(id);
        return HttpStatus.OK;
    }

}

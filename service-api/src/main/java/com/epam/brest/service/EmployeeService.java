package com.epam.brest.service;

import com.epam.brest.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> findAll();

    Optional<Employee> findById(Integer employeeId);

    Optional<Employee> create(Employee employee);

    Optional<Employee> update(Employee employee);

    void delete(Integer employeeId);

}

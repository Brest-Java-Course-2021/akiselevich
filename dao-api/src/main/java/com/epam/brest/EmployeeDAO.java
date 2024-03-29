package com.epam.brest;

import com.epam.brest.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeDAO {

    List<Employee> findAll();

    Optional<Employee> findById(Integer employeeId);

    Optional<Employee> create(Employee employee);

    Optional<Employee> update(Employee employee);

    void delete(Integer employeeId);

}

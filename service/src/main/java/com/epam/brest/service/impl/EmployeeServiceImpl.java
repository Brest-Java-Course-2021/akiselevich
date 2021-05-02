package com.epam.brest.service.impl;

import com.epam.brest.EmployeeDAO;
import com.epam.brest.model.Employee;
import com.epam.brest.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeServiceImpl.class);

    private final EmployeeDAO employeeDAO;

    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Employee> findAll() {
        LOGGER.debug("Service method called to find all Employee");
        return employeeDAO.findAll();
    }

    @Override
    public Optional<Employee> findById(Integer employeeId) {
        LOGGER.debug("Service method called to find Employee by Id: {}", employeeId);
        return employeeDAO.findById(employeeId);
    }

    @Override
    public Optional<Employee> create(Employee employee) {
        LOGGER.debug("Service method called to create Employee: {}", employee);
        return employeeDAO.create(employee);
    }

    @Override
    public Optional<Employee> update(Employee employee) {
        LOGGER.debug("Service method called to update Employee with id: {}, new Employee: {}", employee.getEmployeeId(), employee);
        return employeeDAO.update(employee);
    }

    @Override
    public void delete(Integer employeeId) {
        LOGGER.debug("Service method called to delete Employee by Id: {}", employeeId);
        employeeDAO.delete(employeeId);
    }

}

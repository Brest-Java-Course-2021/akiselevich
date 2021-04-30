package com.epam.brest.service.impl;

import com.epam.brest.EmployeeDTODAO;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeDTOServiceImpl implements EmployeeDTOService {

    private static final Logger LOGGER = LogManager.getLogger(EmployeeDTOServiceImpl.class);

    private final EmployeeDTODAO employeeDTODAO;

    public EmployeeDTOServiceImpl(EmployeeDTODAO employeeDTODAO) {
        this.employeeDTODAO = employeeDTODAO;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        LOGGER.debug("Service method called to find all EmployeeDTO");
        return employeeDTODAO.findAllEmployee();
    }

    @Override
    public Optional<EmployeeDTO> findById(Integer employeeId) {
        LOGGER.debug("Service method called to find EmployeeDTO by Id: {}", employeeId);
        return employeeDTODAO.findEmployeeById(employeeId);
    }

}

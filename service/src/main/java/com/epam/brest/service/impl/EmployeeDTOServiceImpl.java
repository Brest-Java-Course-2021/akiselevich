package com.epam.brest.service.impl;

import com.epam.brest.EmployeeDTODAO;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeDTOServiceImpl implements EmployeeDTOService {

    private static final Log LOGGER = LogFactory.getLog(EmployeeDTOServiceImpl.class);

    private final EmployeeDTODAO employeeDTODAO;

    public EmployeeDTOServiceImpl(EmployeeDTODAO employeeDTODAO) {
        this.employeeDTODAO = employeeDTODAO;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        LOGGER.debug("Service method called to find all EmployeeDTO");
        return employeeDTODAO.findAllEmployee();
    }

}

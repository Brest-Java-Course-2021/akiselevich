package com.epam.brest.service;

import com.epam.brest.model.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeDTOService {

    List<EmployeeDTO> findAll();

    Optional<EmployeeDTO> findById(Integer employeeId);

}

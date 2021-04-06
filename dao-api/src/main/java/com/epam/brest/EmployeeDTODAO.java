package com.epam.brest;

import com.epam.brest.model.dto.EmployeeDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeDTODAO {

    List<EmployeeDTO> findAllEmployee();

    Optional<EmployeeDTO> findEmployeeById(Integer employeeId);

}

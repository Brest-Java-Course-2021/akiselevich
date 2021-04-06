package com.epam.brest.service.impl;

import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import com.epam.brest.test_db.SpringJdbcConfig;
import com.epam.brest.dao.EmployeeJdbcDTODAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({EmployeeDTOServiceImpl.class, EmployeeJdbcDTODAO.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@PropertySource({"classpath:dao.properties"})
@Transactional
class EmployeeDTOServiceImplTest {
    
    @Autowired
    private EmployeeDTOService employeeDTOService;

    @Test
    public void shouldFindAllEmployees(){
        List<EmployeeDTO> employeeDTOList = employeeDTOService.findAll();
        assertTrue(employeeDTOList.size() > 0);
    }

    @Test
    public void shouldFindEmployee(){
        Optional<EmployeeDTO> employeeDTO  = employeeDTOService.findById(1);
        assertTrue(employeeDTO.isPresent());
    }

    @Test
    public void shouldNotFindEmployee(){
        Optional<EmployeeDTO> employeeDTO  = employeeDTOService.findById(10);
        assertTrue(employeeDTO.isEmpty());
    }
}
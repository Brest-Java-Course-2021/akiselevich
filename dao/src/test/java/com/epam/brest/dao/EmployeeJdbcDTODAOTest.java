package com.epam.brest.dao;

import com.epam.brest.SpringJdbcConfig;
import com.epam.brest.dao.EmployeeJdbcDTODAO;
import com.epam.brest.model.dto.EmployeeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({EmployeeJdbcDTODAO.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeJdbcDTODAOTest {

    @Autowired
    private EmployeeJdbcDTODAO employeeDAO;

    @Test
    public void findAllEmployeeTest(){
        List<EmployeeDTO> employees = employeeDAO.findAllEmployee();
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
    }

}
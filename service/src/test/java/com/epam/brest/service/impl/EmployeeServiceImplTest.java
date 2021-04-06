package com.epam.brest.service.impl;

import com.epam.brest.model.Employee;
import com.epam.brest.test_db.SpringJdbcConfig;
import com.epam.brest.dao.EmployeeJdbcDAO;
import com.epam.brest.service.EmployeeService;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import({EmployeeServiceImpl.class, EmployeeJdbcDAO.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@PropertySource({"classpath:dao.properties"})
@Transactional
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void shouldFindAllEmployees(){
        List<Employee> employeeList = employeeService.findAll();
        assertTrue(employeeList.size() > 0);
    }

    @Test
    public void shouldFindEmployee(){
        Optional<Employee> employee = employeeService.findById(1);
        assertTrue(employee.isPresent());
        assertEquals(1, employee.get().getEmployeeId());
    }

    @Test
    public void shouldNotFindEmployee(){
        Optional<Employee> employee = employeeService.findById(10);
        assertTrue(employee.isEmpty());
    }

    @Test
    public void shouldCreateNewEmployee(){
        Employee newEmployee = new Employee(
                null,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "abc@mail.ru",
                null);
        Optional<Employee> employee = employeeService.create(newEmployee);
        assertTrue(employee.isPresent());
        assertNotNull(employee.get().getEmployeeId());
    }

    @Test
    public void shouldNotCreateNewEmployee(){
        Employee newEmployee = new Employee(
                null,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "ivanov_ivan@gmail.com",
                null);
        assertThrows(IllegalArgumentException.class, () -> employeeService.create(newEmployee));
    }

    @Test
    public void shouldUpdateExistEmployee(){
        Employee newEmployee = new Employee(
                1,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "abc@mail.ru",
                null);
        Optional<Employee> employee = employeeService.update(newEmployee);
        assertTrue(employee.isPresent());
        assertEquals("abc@mail.ru", employee.get().getEmail());
    }

    @Test
    public void shouldNotUpdateNotExistEmployee(){
        Employee newEmployee = new Employee(
                999,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "abc@mail.ru",
                null);
        assertThrows(IllegalArgumentException.class, () -> employeeService.update(newEmployee));
    }

    @Test
    public void shouldNotUpdateExistEmployee(){
        Employee newEmployee = new Employee(
                999,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "ivanov_ivan@gmail.com",
                null);
        assertThrows(IllegalArgumentException.class, () -> employeeService.update(newEmployee));
    }

    @Test
    public void shouldNotDeleteNotExistEmployee(){
        assertThrows(IllegalArgumentException.class, () -> employeeService.delete(999));
    }

    @Test
    public void shouldDeleteExistEmployee(){
        assertDoesNotThrow(() -> employeeService.delete(1));
    }

}
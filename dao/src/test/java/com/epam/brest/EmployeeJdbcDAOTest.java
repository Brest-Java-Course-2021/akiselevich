package com.epam.brest;

import com.epam.brest.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({EmployeeJdbcDAO.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeJdbcDAOTest {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Test
    public void findAllEmployeeTest(){
        List<Employee> employees = employeeDAO.findAll();
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
    }

    @Test
    public void findExistEmployeeTest(){
        Optional<Employee> employee = employeeDAO.findById(1);
        assertTrue(employee.isPresent());
        assertEquals(1, (int) employee.get().getEmployeeId());
    }

    @Test
    public void findNotExistEmployeeTest(){
        Optional<Employee> employee = employeeDAO.findById(9999);
        assertTrue(employee.isEmpty());
    }

    @Test
    public void createNewEmployeeTest(){
        Employee newEmployee = new Employee(
                null,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivan@mail.ru",
                List.of(1,2));
        Optional<Employee> createdEmployee = employeeDAO.create(newEmployee);
        assertTrue(createdEmployee.isPresent());
        assertEquals("ivan@mail.ru", createdEmployee.get().getEmail());
    }

    @Test
    public void createNewEmployeeWithSameEmailTest(){
        Employee newEmployee = new Employee(
                null,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivanov_ivan@gmail.com",
                List.of(1,2));
        assertThrows(IllegalArgumentException.class, () -> {
            Optional<Employee> createdEmployee = employeeDAO.create(newEmployee);
        });
    }

    @Test
    public void createNewEmployeeWithoutEmployeeTest(){
        Employee newEmployee = new Employee(
                null,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivan@mail.ru",
                null);
        Optional<Employee> createdEmployee = employeeDAO.create(newEmployee);
        assertTrue(createdEmployee.isPresent());
        assertEquals("ivan@mail.ru", createdEmployee.get().getEmail());
    }

    @Test
    public void createNewEmployeeWithNotExistRoleTest(){
        Employee newEmployee = new Employee(
                null,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivan@mail.ru",
                List.of(9999,999));
        assertThrows(IllegalArgumentException.class, () -> {
            Optional<Employee> createdEmployee = employeeDAO.create(newEmployee);
        });
    }

    @Test
    public void updateExistEmployee(){
        Employee newEmployee = new Employee(
                1,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivan@mail.ru",
                List.of(1,2));
        Optional<Employee> createdEmployee = employeeDAO.update(newEmployee);
        assertTrue(createdEmployee.isPresent());
        assertEquals("ivan@mail.ru", employeeDAO.findById(1).get().getEmail());
    }

    @Test
    public void updateNotExistEmployee(){
        Employee newEmployee = new Employee(
                999,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivan@mail.ru",
                List.of(1,2));
        assertThrows(IllegalArgumentException.class, () -> {
            employeeDAO.update(newEmployee);
        });
    }

    @Test
    public void updateExistEmployeeWithNotExistRole(){
        Employee newEmployee = new Employee(
                1,
                "ivan",
                "Ivanov",
                "ivanovich",
                "ivan@mail.ru",
                List.of(9999,999));
        assertThrows(IllegalArgumentException.class, () -> {
            Optional<Employee> createdEmployee = employeeDAO.update(newEmployee);
        });
    }

    @Test
    public void deleteExistEmployee(){
        employeeDAO.delete(1);
        assertEquals(2,employeeDAO.findAll().size());
    }

    @Test
    public void deleteNotExistEmployee(){
        assertThrows(IllegalArgumentException.class, () -> {
            employeeDAO.delete(999);
        });
        assertEquals(3,employeeDAO.findAll().size());
    }


}
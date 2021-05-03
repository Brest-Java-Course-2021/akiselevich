package com.epam.brest.service.rest;

import com.epam.brest.model.Employee;
import com.epam.brest.service.EmployeeService;
import com.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.service.rest.config.TestConfig.EMPLOYEES_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class EmployeeServiceRestTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    EmployeeService employeeService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void shouldFindAllEmployees() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EMPLOYEES_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createEmployee(0),
                                createEmployee(1)))));

        List<Employee> employees = employeeService.findAll();

        mockServer.verify();
        assertNotNull(employees);
        assertTrue(employees.size() > 0);
    }

    @Test
    public void shouldFindEmployeeById() throws Exception {
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EMPLOYEES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createEmployee(id))));

        Optional<Employee> optionalEmployee = employeeService.findById(id);

        mockServer.verify();
        assertTrue(optionalEmployee.isPresent());
        assertEquals(optionalEmployee.get().getEmployeeId(), id);
    }

    @Test
    public void shouldCreateEmployee() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EMPLOYEES_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createEmployee(1))));

        Optional<Employee> optionalEmployee = employeeService.create(createEmployee(null));

        mockServer.verify();
        assertTrue(optionalEmployee.isPresent());
        assertEquals(optionalEmployee.get().getEmployeeId(), 1);
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {

        Integer id = 1;
        Employee employee = createEmployee(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EMPLOYEES_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(employee)));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(EMPLOYEES_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(employee)));

        Optional<Employee> optionalEmployee = employeeService.update(employee);
        Optional<Employee> updatedEmployee = employeeService.findById(id);

        mockServer.verify();
        assertTrue(optionalEmployee.isPresent());
        assertTrue(updatedEmployee.isPresent());
        assertEquals(updatedEmployee.get().getEmployeeId(), id);
        assertEquals(updatedEmployee.get().getEmail(), employee.getEmail());
    }

    private Employee createEmployee(Integer index) {
        Employee employee = new Employee();
        employee.setEmployeeId(index);
        employee.setFirstName("f" + index);
        employee.setLastName("l" + index);
        employee.setMiddleName("m" + index);
        employee.setEmail(index + "@mail.ru");
        return employee;
    }

}
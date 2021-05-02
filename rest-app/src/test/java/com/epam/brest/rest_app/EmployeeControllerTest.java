package com.epam.brest.rest_app;

import com.epam.brest.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeControllerTest {

    private final String EMPLOYEE_ENDPOINT = "/employees";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllEmployees() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(EMPLOYEE_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        List<Employee> employees = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(employees.size() > 0);
    }

    @Test
    void shouldReturnEmployeeById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(EMPLOYEE_ENDPOINT + "/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Employee employee = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, employee.getEmployeeId());
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(
                get(EMPLOYEE_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewEmployee() throws Exception {
        Employee newEmployee = new Employee(
                null,
                "abc",
                "abc",
                "abc",
                "abc@mail.com",
                null);
        String json = objectMapper.writeValueAsString(newEmployee);
        MockHttpServletResponse response = mockMvc.perform(
                post(EMPLOYEE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        assertNotNull(response);
        Employee employee = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newEmployee.getFirstName(), employee.getFirstName());
    }

    @Test
    void shouldNotCreateNewEmployee() throws Exception {
        Employee newEmployee = new Employee(
                null,
                "abc",
                "abc",
                "abc",
                "ivanov_ivan@gmail.com",
                null);
        String json = objectMapper.writeValueAsString(newEmployee);
        mockMvc.perform(
                post(EMPLOYEE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateEmployee() throws Exception {
        Employee newEmployee = new Employee(
                1,
                "abc",
                "abc",
                "abc",
                "abc@mail.com",
                null);
        String json = objectMapper.writeValueAsString(newEmployee);
        MockHttpServletResponse response = mockMvc.perform(
                put(EMPLOYEE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Employee employee = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newEmployee.getEmployeeId(), employee.getEmployeeId());
        assertEquals(newEmployee.getFirstName(), employee.getFirstName());
    }

    @Test
    void shouldNotUpdateEmployee() throws Exception {
        Employee newEmployee = new Employee(
                9999,
                "abc",
                "abc",
                "abc",
                "abc@mail.com",
                null);
        String json = objectMapper.writeValueAsString(newEmployee);
        mockMvc.perform(
                put(EMPLOYEE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteEmployee() throws Exception {
        mockMvc.perform(
                delete(EMPLOYEE_ENDPOINT + "/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteEmployee() throws Exception {
        mockMvc.perform(
                delete(EMPLOYEE_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}
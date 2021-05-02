package com.epam.brest.rest_app;

import com.epam.brest.model.dto.EmployeeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EmployeeDTOControllerTest {

    private final String EMPLOYEE_ENDPOINT = "/employee-dtos";

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
        List<EmployeeDTO> employees = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
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
        EmployeeDTO employee = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, employee.getEmployeeId());
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(
                get(EMPLOYEE_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}
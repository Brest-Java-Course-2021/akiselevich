package com.epam.brest.web_app;

import com.epam.brest.model.Employee;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.service.EmployeeDTOService;
import com.epam.brest.service.EmployeeService;
import com.epam.brest.service.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeDTOService employeeDTOService;

    @MockBean
    private RoleService roleService;

    @Captor
    private ArgumentCaptor<Employee> captor;

    @Test
    public void shouldReturnEmployeesPage() throws Exception {
        EmployeeDTO employee1 = new EmployeeDTO(
                1,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "ivan@mail.ru",
                null);
        EmployeeDTO employee2 = new EmployeeDTO(
                2,
                "Peter",
                "Petrov",
                "Petrovich",
                "peter@mail.ru",
                null);

        Mockito.when(employeeDTOService.findAll())
                .thenReturn(Arrays.asList(employee1, employee2));

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("employees"))
                .andExpect(model().attribute("employees", hasItem(
                        allOf(
                                hasProperty("employeeId", is(employee1.getEmployeeId())),
                                hasProperty("firstName", is(employee1.getFirstName())),
                                hasProperty("lastName", is(employee1.getLastName())),
                                hasProperty("middleName", is(employee1.getMiddleName())),
                                hasProperty("email", is(employee1.getEmail())),
                                hasProperty("roles", is(employee1.getRoles()))
                        )
                )))
                .andExpect(model().attribute("employees", hasItem(
                        allOf(
                                hasProperty("employeeId", is(employee2.getEmployeeId())),
                                hasProperty("firstName", is(employee2.getFirstName())),
                                hasProperty("lastName", is(employee2.getLastName())),
                                hasProperty("middleName", is(employee2.getMiddleName())),
                                hasProperty("email", is(employee2.getEmail())),
                                hasProperty("roles", is(employee2.getRoles()))
                        )
                )));
    }

    @Test
    public void shouldOpenNewEmployeePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employee"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("employee"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("employee", isA(Employee.class)));
        verifyNoMoreInteractions(employeeService, employeeDTOService);
    }

    @Test
    public void shouldAddNewEmployee() throws Exception {
        Employee newEmployee = new Employee(
                null,
                randomAlphabetic(20),
                randomAlphabetic(20),
                randomAlphabetic(20),
                randomAlphabetic(7)+"@"+ randomAlphabetic(7)+"." + randomAlphabetic(3),
                new ArrayList<>());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/employee")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", newEmployee.getFirstName())
                        .param("lastName", newEmployee.getLastName())
                        .param("middleName", newEmployee.getMiddleName())
                        .param("email", newEmployee.getEmail()))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employees"))
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).create(captor.capture());

        Employee employee = captor.getValue();
        assertEquals(newEmployee.getFirstName(), employee.getFirstName());
        assertEquals(newEmployee.getLastName(), employee.getLastName());
        assertEquals(newEmployee.getMiddleName(), employee.getMiddleName());
        assertEquals(newEmployee.getEmail(), employee.getEmail());
    }

    @Test
    public void shouldOpenEditEmployeePageById() throws Exception {
        EmployeeDTO employee = new EmployeeDTO(
                1,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "ivan@mail.ru",
                null);
        Mockito.when(employeeDTOService.findById(any()))
                .thenReturn(Optional.of(employee));
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + employee.getEmployeeId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("employee"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(
                        model().attribute(
                                "employee",
                                hasProperty("employeeId", is(employee.getEmployeeId()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "employee",
                                hasProperty("firstName", is(employee.getFirstName()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "employee",
                                hasProperty("lastName", is(employee.getLastName()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "employee",
                                hasProperty("middleName", is(employee.getMiddleName()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "employee",
                                hasProperty("email", is(employee.getEmail()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "employee",
                                hasProperty("roleId", is(employee.getRoles()))
                        )
                );
    }

    @Test
    public void shouldReturnErrorPageIfEmployeeNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
        verify(employeeDTOService).findById(id);
    }

    @Test
    public void shouldUpdateEmployeeAfterEdit() throws Exception {
        Employee newEmployee = new Employee(
                1,
                randomAlphabetic(20),
                randomAlphabetic(20),
                randomAlphabetic(20),
                randomAlphabetic(7)+"@"+ randomAlphabetic(7)+"." + randomAlphabetic(3),
                new ArrayList<>());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/employee/" + newEmployee.getEmployeeId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("employeeId", newEmployee.getEmployeeId().toString())
                        .param("firstName", newEmployee.getFirstName())
                        .param("lastName", newEmployee.getLastName())
                        .param("middleName", newEmployee.getMiddleName())
                        .param("email", newEmployee.getEmail())
                        .param("name", Arrays.toString(newEmployee.getRoleId().toArray())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employees"))
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).update(captor.capture());

        Employee employee = captor.getValue();
        assertEquals(newEmployee.getFirstName(), employee.getFirstName());
        assertEquals(newEmployee.getLastName(), employee.getLastName());
        assertEquals(newEmployee.getMiddleName(), employee.getMiddleName());
        assertEquals(newEmployee.getEmail(), employee.getEmail());
        assertNull(employee.getRoleId());
    }

    @Test
    public void shouldDeleteEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/employee/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/employees"))
                .andExpect(redirectedUrl("/employees"));
        verify(employeeService).delete(1);
    }

}
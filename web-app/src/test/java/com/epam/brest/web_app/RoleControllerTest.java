package com.epam.brest.web_app;

import com.epam.brest.model.Role;
import com.epam.brest.service.RoleService;
import org.apache.commons.lang3.RandomStringUtils;
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

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Captor
    private ArgumentCaptor<Role> captor;

    @Test
    public void shouldReturnRolesPage() throws Exception {
        Role role1 = new Role(1, "QA");
        Role role2 = new Role(2, "Java Software Engineer");
        Role role3 = new Role(3, "Product Manager");

        Mockito.when(roleService.findAll())
                .thenReturn(Arrays.asList(role1, role2, role3));

        mockMvc.perform(MockMvcRequestBuilders.get("/roles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("roles"))
                .andExpect(model().attribute("roles", hasItem(
                        allOf(
                                hasProperty("roleId", is(role1.getRoleId())),
                                hasProperty("roleName", is(role1.getRoleName()))
                        )
                )))
                .andExpect(model().attribute("roles", hasItem(
                        allOf(
                                hasProperty("roleId", is(role2.getRoleId())),
                                hasProperty("roleName", is(role2.getRoleName()))
                        )
                )))
                .andExpect(model().attribute("roles", hasItem(
                        allOf(
                                hasProperty("roleId", is(role3.getRoleId())),
                                hasProperty("roleName", is(role3.getRoleName())))
                )));
    }

    @Test
    public void shouldOpenNewRolePage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/role"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("role"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("role", isA(Role.class)));
        verifyNoMoreInteractions(roleService);
    }

    @Test
    public void shouldAddNewRole() throws Exception {
        String testName = RandomStringUtils.randomAlphabetic(45);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/role")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("roleName", testName))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/roles"))
                .andExpect(redirectedUrl("/roles"));

        verify(roleService).create(captor.capture());

        Role role = captor.getValue();
        assertEquals(testName, role.getRoleName());
    }

    @Test
    public void shouldOpenEditRolePageById() throws Exception {
        Role role = new Role(1, "QA");
        Mockito.when(roleService.findById(any())).thenReturn(Optional.of(role));
        mockMvc.perform(MockMvcRequestBuilders.get("/role/" + role.getRoleId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("role"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(
                        model().attribute(
                                "role",
                                hasProperty("roleId", is(role.getRoleId()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "role",
                                hasProperty("roleName", is(role.getRoleName()))
                        )
                );
    }

    @Test
    public void shouldReturnErrorPageIfRoleNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(MockMvcRequestBuilders.get("/role/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
        verify(roleService).findById(id);
    }

    @Test
    public void shouldUpdateRoleAfterEdit() throws Exception {
        String testName = RandomStringUtils.randomAlphabetic(45);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/role/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("roleId", "1")
                        .param("roleName", testName))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/roles"))
                .andExpect(redirectedUrl("/roles"));

        verify(roleService).update(captor.capture());

        Role role = captor.getValue();
        assertEquals(testName, role.getRoleName());
    }

    @Test
    public void shouldDeleteRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/role/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/roles"))
                .andExpect(redirectedUrl("/roles"));
        verify(roleService).delete(1);
    }

}
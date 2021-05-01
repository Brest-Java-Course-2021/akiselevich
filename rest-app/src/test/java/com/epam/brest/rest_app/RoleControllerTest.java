package com.epam.brest.rest_app;

import com.epam.brest.model.Role;
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
class RoleControllerTest {

    private final String ROLE_ENDPOINT = "/roles";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllRoles() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(ROLE_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        List<Role> roles = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(roles.size() > 0);
    }

    @Test
    void shouldReturnRoleById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(ROLE_ENDPOINT + "/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Role role = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, role.getRoleId());
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(
                get(ROLE_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewRole() throws Exception {
        Role newRole = new Role(null, "abc");
        String json = objectMapper.writeValueAsString(newRole);
        MockHttpServletResponse response = mockMvc.perform(
                post(ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        assertNotNull(response);
        Role role = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newRole.getRoleName(), role.getRoleName());
    }

    @Test
    void shouldUpdateRole() throws Exception {
        Role newRole = new Role(1, "abc");
        String json = objectMapper.writeValueAsString(newRole);
        MockHttpServletResponse response = mockMvc.perform(
                put(ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Role role = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newRole.getRoleId(), role.getRoleId());
        assertEquals(newRole.getRoleName(), role.getRoleName());
    }

    @Test
    void shouldNotUpdateRole() throws Exception {
        Role newRole = new Role(9999, "abc");
        String json = objectMapper.writeValueAsString(newRole);
        mockMvc.perform(
                put(ROLE_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteRole() throws Exception {
        mockMvc.perform(
                delete(ROLE_ENDPOINT + "/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteRole() throws Exception {
        mockMvc.perform(
                delete(ROLE_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}
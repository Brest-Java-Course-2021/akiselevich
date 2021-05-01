package com.epam.brest.rest_app;

import com.epam.brest.model.dto.ProjectDTO;
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
class ProjectDTOControllerTest {

    private final String PROJECT_ENDPOINT = "/project-dtos";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllProjects() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(PROJECT_ENDPOINT))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        List<ProjectDTO> projects = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertTrue(projects.size() > 0);
    }

    @Test
    void shouldReturnProjectById() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                get(PROJECT_ENDPOINT + "/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        ProjectDTO project = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, project.getProjectId());
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(
                get(PROJECT_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}
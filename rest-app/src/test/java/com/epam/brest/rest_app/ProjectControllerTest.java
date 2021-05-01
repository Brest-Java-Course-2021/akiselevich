package com.epam.brest.rest_app;

import com.epam.brest.model.Project;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProjectControllerTest {

    private final String PROJECT_ENDPOINT = "/projects";

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
        List<Project> projects = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
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
        Project project = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(1, project.getProjectId());
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        mockMvc.perform(
                get(PROJECT_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewProject() throws Exception {
        Project newProject = new Project(
                null,
                "abc",
                LocalDate.of(2000,2,15),
                LocalDate.of(2000,2,19),
                null);
        String json = objectMapper.writeValueAsString(newProject);
        MockHttpServletResponse response = mockMvc.perform(
                post(PROJECT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andReturn().getResponse();
        assertNotNull(response);
        Project project = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newProject.getProjectName(), project.getProjectName());
    }

    @Test
    void shouldUpdateProject() throws Exception {
        Project newProject = new Project(
                1,
                "abc",
                LocalDate.of(2000,2,15),
                LocalDate.of(2000,2,19),
                null);
        String json = objectMapper.writeValueAsString(newProject);
        MockHttpServletResponse response = mockMvc.perform(
                put(PROJECT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertNotNull(response);
        Project project = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newProject.getProjectId(), project.getProjectId());
        assertEquals(newProject.getProjectName(), project.getProjectName());
    }

    @Test
    void shouldNotUpdateProject() throws Exception {
        Project newProject = new Project(
                9999,
                "abc",
                LocalDate.of(2000,2,15),
                LocalDate.of(2000,2,19),
                null);
        String json = objectMapper.writeValueAsString(newProject);
        mockMvc.perform(
                put(PROJECT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteProject() throws Exception {
        mockMvc.perform(
                delete(PROJECT_ENDPOINT + "/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteProject() throws Exception {
        mockMvc.perform(
                delete(PROJECT_ENDPOINT + "/9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}
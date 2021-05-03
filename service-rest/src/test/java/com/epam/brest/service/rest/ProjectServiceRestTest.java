package com.epam.brest.service.rest;

import com.epam.brest.model.Project;
import com.epam.brest.service.ProjectService;
import com.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.service.rest.config.TestConfig.PROJECTS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ProjectServiceRestTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProjectService projectService;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void shouldFindAllProjects() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createProject(0),
                                createProject(1)))));

        List<Project> projects = projectService.findAll();

        mockServer.verify();
        assertNotNull(projects);
        assertTrue(projects.size() > 0);
    }

    @Test
    public void shouldFindProjectById() throws Exception {
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createProject(id))));

        Optional<Project> optionalProject = projectService.findById(id);

        mockServer.verify();
        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get().getProjectId(), id);
    }

    @Test
    public void shouldCreateProject() throws Exception {
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createProject(1))));

        Optional<Project> optionalProject = projectService.create(createProject(null));

        mockServer.verify();
        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get().getProjectId(), 1);
    }

    @Test
    public void shouldUpdateProject() throws Exception {

        Integer id = 1;
        Project project = createProject(id);

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL)))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(project)));

        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(project)));

        Optional<Project> optionalProject = projectService.update(project);
        Optional<Project> updatedProject = projectService.findById(id);

        mockServer.verify();
        assertTrue(optionalProject.isPresent());
        assertTrue(updatedProject.isPresent());
        assertEquals(updatedProject.get().getProjectId(), id);
        assertEquals(updatedProject.get().getProjectName(), project.getProjectName());
    }

    private Project createProject(Integer index) {
        Project project = new Project();
        project.setProjectId(index);
        project.setProjectName("project" + index);
        project.setStartDate(LocalDate.of(2000, 2, 15));
        project.setFinishDate(LocalDate.of(2000, 2, 17));
        return project;
    }

}
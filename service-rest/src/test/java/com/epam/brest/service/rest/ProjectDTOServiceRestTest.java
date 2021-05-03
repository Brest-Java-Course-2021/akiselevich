package com.epam.brest.service.rest;

import com.epam.brest.model.Filter;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import com.epam.brest.service.rest.config.TestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.client.utils.URIBuilder;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.service.rest.config.TestConfig.PROJECTS_DTOS_URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ProjectDTOServiceRestTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ProjectDTOService projectDTOService;

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
        URIBuilder uri = new URIBuilder(PROJECTS_DTOS_URL);
        uri.addParameter("startDate", LocalDate.of(2000, 2, 15).format(DateTimeFormatter.ISO_DATE))
                .addParameter("finishDate", LocalDate.of(2000, 2, 17).format(DateTimeFormatter.ISO_DATE));
        mockServer.expect(ExpectedCount.once(), requestTo(uri.build()))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(
                                createProject(0),
                                createProject(1)))));

        List<ProjectDTO> projects =
                projectDTOService.findAll(
                        new Filter(
                                LocalDate.of(2000, 2, 15),
                                LocalDate.of(2000, 2, 17)));

        mockServer.verify();
        assertNotNull(projects);
        assertTrue(projects.size() > 0);
    }

    @Test
    public void shouldFindProjectById() throws Exception {
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(PROJECTS_DTOS_URL + "/" + id)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(createProject(id))));

        Optional<ProjectDTO> optionalProject = projectDTOService.findById(id);

        mockServer.verify();
        assertTrue(optionalProject.isPresent());
        assertEquals(optionalProject.get().getProjectId(), id);
    }

    private ProjectDTO createProject(Integer index) {
        ProjectDTO project = new ProjectDTO();
        project.setProjectId(index);
        project.setProjectName("project" + index);
        project.setStartDate(LocalDate.of(2000, 2, 15));
        project.setFinishDate(LocalDate.of(2000, 2, 17));
        return project;
    }

}
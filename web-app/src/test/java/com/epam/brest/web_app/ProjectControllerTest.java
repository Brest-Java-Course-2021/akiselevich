package com.epam.brest.web_app;

import com.epam.brest.model.Filter;
import com.epam.brest.model.Project;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.EmployeeService;
import com.epam.brest.service.ProjectDTOService;
import com.epam.brest.service.ProjectService;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectDTOService projectDTOService;

    @MockBean
    private EmployeeService employeeService;

    @Captor
    private ArgumentCaptor<Project> captor;

    @Test
    public void shouldReturnProjectsPage() throws Exception {
        ProjectDTO project1 = new ProjectDTO(
                1,
                "Project1",
                LocalDate.parse("2020-05-05", DateTimeFormatter.ISO_DATE),
                LocalDate.parse("2020-05-06", DateTimeFormatter.ISO_DATE),
                null,
                0);
        ProjectDTO project2 = new ProjectDTO(
                2,
                "Project2",
                LocalDate.parse("2020-05-05", DateTimeFormatter.ISO_DATE),
                LocalDate.parse("2020-05-06", DateTimeFormatter.ISO_DATE),
                null,
                0);
        Mockito.when(projectDTOService.findAll(new Filter()))
                .thenReturn(Arrays.asList(project1, project2));

        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("projects"))
                .andExpect(model().attribute("projects", hasItem(
                        allOf(
                                hasProperty("projectId", is(project1.getProjectId())),
                                hasProperty("projectName", is(project1.getProjectName())),
                                hasProperty("startDate", is(project1.getStartDate())),
                                hasProperty("finishDate", is(project1.getFinishDate())),
                                hasProperty("employees", is(project1.getEmployees())),
                                hasProperty("numberOfEmployees", is(project1.getNumberOfEmployees()))
                        )
                )))
                .andExpect(model().attribute("projects", hasItem(
                        allOf(
                                hasProperty("projectId", is(project2.getProjectId())),
                                hasProperty("projectName", is(project2.getProjectName())),
                                hasProperty("startDate", is(project2.getStartDate())),
                                hasProperty("finishDate", is(project2.getFinishDate())),
                                hasProperty("employees", is(project2.getEmployees())),
                                hasProperty("numberOfEmployees", is(project2.getNumberOfEmployees()))
                        )
                )));
    }

    @Test
    public void shouldOpenNewProjectPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/project"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("project"))
                .andExpect(model().attribute("isNew", is(true)))
                .andExpect(model().attribute("project", isA(Project.class)));
        verifyNoMoreInteractions(projectService, projectDTOService);
    }

    @Test
    public void shouldAddNewProject() throws Exception {
        Project project = new Project(
                null,
                "Project1",
                LocalDate.parse("2020-05-05", DateTimeFormatter.ISO_DATE),
                LocalDate.parse("2020-05-06", DateTimeFormatter.ISO_DATE),
                new ArrayList<>());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("projectName", project.getProjectName())
                        .param("startDate", project.getStartDate().format(DateTimeFormatter.ISO_DATE))
                        .param("finishDate", project.getFinishDate().format(DateTimeFormatter.ISO_DATE))
                        .param("employees", Arrays.toString(project.getEmployeeId().toArray())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        verify(projectService).create(captor.capture());

        Project newProject = captor.getValue();
        assertEquals(project.getProjectName(), newProject.getProjectName());
        assertEquals(project.getStartDate(), newProject.getStartDate());
        assertEquals(project.getFinishDate(), newProject.getFinishDate());
    }

    @Test
    public void shouldOpenEditProjectPageById() throws Exception {
        ProjectDTO project = new ProjectDTO(
                1,
                "Project1",
                LocalDate.parse("2020-05-05", DateTimeFormatter.ISO_DATE),
                LocalDate.parse("2020-05-06", DateTimeFormatter.ISO_DATE),
                null,
                0);
        Mockito.when(projectDTOService.findById(any()))
                .thenReturn(Optional.of(project));
        mockMvc.perform(MockMvcRequestBuilders.get("/project/" + project.getProjectId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("project"))
                .andExpect(model().attribute("isNew", is(false)))
                .andExpect(
                        model().attribute(
                                "project",
                                hasProperty("projectId", is(project.getProjectId()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "project",
                                hasProperty("projectName", is(project.getProjectName()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "project",
                                hasProperty("startDate", is(project.getStartDate()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "project",
                                hasProperty("finishDate", is(project.getFinishDate()))
                        )
                )
                .andExpect(
                        model().attribute(
                                "project",
                                hasProperty("employeeId", is(project.getEmployees()))
                        )
                );
    }

    @Test
    public void shouldReturnErrorPageIfProjectNotFoundById() throws Exception {
        int id = 99999;
        mockMvc.perform(MockMvcRequestBuilders.get("/project/" + id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
        verify(projectDTOService).findById(id);
    }

    @Test
    public void shouldUpdateProjectAfterEdit() throws Exception {
        Project newProject = new Project(
                1,
                "Project1",
                LocalDate.parse("2020-05-05", DateTimeFormatter.ISO_DATE),
                LocalDate.parse("2020-05-06", DateTimeFormatter.ISO_DATE),
                new ArrayList<>());
        mockMvc.perform(
                MockMvcRequestBuilders.post("/project/" + newProject.getProjectId())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("projectId", newProject.getProjectId().toString())
                        .param("projectName", newProject.getProjectName())
                        .param("startDate", newProject.getStartDate().format(DateTimeFormatter.ISO_DATE))
                        .param("finishDate", newProject.getFinishDate().format(DateTimeFormatter.ISO_DATE))
                        .param("employees", Arrays.toString(newProject.getEmployeeId().toArray())))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));

        verify(projectService).update(captor.capture());

        Project project = captor.getValue();
        assertEquals(newProject.getProjectName(), project.getProjectName());
        assertEquals(newProject.getStartDate(), project.getStartDate());
        assertEquals(newProject.getFinishDate(), project.getFinishDate());
        assertNull(project.getEmployeeId());
    }

    @Test
    public void shouldDeleteProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/project/1/delete"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/projects"))
                .andExpect(redirectedUrl("/projects"));
        verify(projectService).delete(1);
    }

}
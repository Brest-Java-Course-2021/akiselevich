package com.epam.brest.service.impl;

import com.epam.brest.dao.ProjectJdbcDAO;
import com.epam.brest.model.Project;
import com.epam.brest.service.ProjectService;
import com.epam.brest.test_db.SpringJdbcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SpringJdbcConfig.class)
@Import({ProjectServiceImpl.class, ProjectJdbcDAO.class})
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@PropertySource({"classpath:dao.properties"})
@Transactional
class ProjectServiceImplTest {

    @Autowired
    private ProjectService projectService;

    @Test
    public void shouldFindAllProjects(){
        List<Project> projectList = projectService.findAll();
        assertTrue(projectList.size() > 0);
    }

    @Test
    public void shouldFindProject(){
        Optional<Project> project = projectService.findById(1);
        assertTrue(project.isPresent());
        assertEquals(1, project.get().getProjectId());
    }

    @Test
    public void shouldNotFindProject(){
        Optional<Project> project = projectService.findById(10);
        assertTrue(project.isEmpty());
    }

    @Test
    public void shouldCreateNewProject(){
        Project newProject = new Project(
                null,
                "abc",
                LocalDate.now(),
                LocalDate.now(),
                null);
        Optional<Project> project = projectService.create(newProject);
        assertTrue(project.isPresent());
        assertNotNull(project.get().getProjectId());
    }

    @Test
    public void shouldNotCreateNewProject(){
        Project newProject = new Project(
                null,
                "abc",
                LocalDate.now(),
                LocalDate.now(),
                List.of(9999, 999));
        assertThrows(IllegalArgumentException.class, () -> projectService.create(newProject));
    }

    @Test
    public void shouldUpdateExistProject(){
        Project newProject = new Project(
                1,
                "abc",
                LocalDate.now(),
                LocalDate.now(),
                null);
        Optional<Project> project = projectService.update(newProject);
        assertTrue(project.isPresent());
        assertEquals("abc", project.get().getProjectName());
    }

    @Test
    public void shouldNotUpdateNotExistProject(){
        Project newProject = new Project(
                9999,
                "abc",
                LocalDate.now(),
                LocalDate.now(),
                null);
        assertThrows(IllegalArgumentException.class, () -> projectService.update(newProject));
    }

    @Test
    public void shouldNotUpdateExistProject(){
        Project newProject = new Project(
                1,
                "abc",
                LocalDate.now(),
                LocalDate.now(),
                List.of(9999, 999));
        assertThrows(IllegalArgumentException.class, () -> projectService.update(newProject));
    }

    @Test
    public void shouldNotDeleteNotExistProject(){
        assertThrows(IllegalArgumentException.class, () -> projectService.delete(999));
    }

    @Test
    public void shouldDeleteExistProject(){
        assertDoesNotThrow(() -> projectService.delete(1));
    }

}
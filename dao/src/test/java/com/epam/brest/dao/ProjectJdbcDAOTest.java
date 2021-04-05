package com.epam.brest.dao;

import com.epam.brest.ProjectDAO;
import com.epam.brest.SpringJdbcConfig;
import com.epam.brest.dao.ProjectJdbcDAO;
import com.epam.brest.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({ProjectJdbcDAO.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectJdbcDAOTest {

    @Autowired
    private ProjectDAO projectDAO;

    @Test
    public void findAllProjectsTest(){
       List<Project> projects = projectDAO.findAll();
       assertNotNull(projects);
       assertTrue(projects.size() > 0);
    }

    @Test
    public void findExistProjectTest(){
        Optional<Project> project = projectDAO.findById(1);
        assertTrue(project.isPresent());
        assertEquals(1, (int) project.get().getProjectId());
    }

    @Test
    public void findNotExistProjectTest(){
        Optional<Project> project = projectDAO.findById(5);
        assertTrue(project.isEmpty());
    }

    @Test
    public void createNewProjectTest(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project newProject = new Project(null,"abc", currentTime, currentTime, List.of(1,2));
        Optional<Project> createdProject = projectDAO.create(newProject);
        assertTrue(createdProject.isPresent());
        assertEquals("abc", createdProject.get().getProjectName());
    }

    @Test
    public void createNewProjectWithoutEmployeeTest(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project newProject = new Project(null,"abc", currentTime, currentTime, null);
        Optional<Project> createdProject = projectDAO.create(newProject);
        assertTrue(createdProject.isPresent());
        assertEquals("abc", createdProject.get().getProjectName());
    }

    @Test
    public void createNewProjectWithNotExistEmployeeTest(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project newProject = new Project(null,"abc", currentTime, currentTime, List.of(9999,999));
        assertThrows(IllegalArgumentException.class, () -> {
            Optional<Project> createdProject = projectDAO.create(newProject);
        });
    }

    @Test
    public void updateExistProject(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project newProject = new Project(1,"abc", currentTime, currentTime, List.of(2,3));
        Optional<Project> createdProject = projectDAO.update(newProject);
        assertTrue(createdProject.isPresent());
        assertEquals("abc",projectDAO.findById(1).get().getProjectName());
    }

    @Test
    public void updateNotExistProject(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project newProject = new Project(9999,"abc", currentTime, currentTime, List.of(2,3));
        assertThrows(IllegalArgumentException.class, () -> {
            projectDAO.update(newProject);
        });
    }

    @Test
    public void updateExistProjectWithNotExistEmployee(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project newProject = new Project(1,"abc", currentTime, currentTime, List.of(9999,999));
        assertThrows(IllegalArgumentException.class, () -> {
            Optional<Project> createdProject = projectDAO.update(newProject);
        });
    }

    @Test
    public void deleteExistProject(){
        projectDAO.delete(1);
        assertEquals(1,projectDAO.findAll().size());
    }

    @Test
    public void deleteNotExistProject(){
        assertThrows(IllegalArgumentException.class, () -> {
            projectDAO.delete(5);
        });
        assertEquals(2,projectDAO.findAll().size());
    }

}
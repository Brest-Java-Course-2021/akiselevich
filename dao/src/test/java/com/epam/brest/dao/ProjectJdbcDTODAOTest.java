package com.epam.brest.dao;

import com.epam.brest.test_db.SpringJdbcConfig;
import com.epam.brest.model.dto.ProjectDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import({ProjectJdbcDTODAO.class})
@PropertySource({"classpath:dao.properties"})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProjectJdbcDTODAOTest {

    @Autowired
    private ProjectJdbcDTODAO projectDAO;

    @Test
    public void findAllProjectTest(){
        List<ProjectDTO> projects = projectDAO.findAllProjectWithEmployeeCount(null);
        assertNotNull(projects);
        assertTrue(projects.size() > 0);
    }

    @Test
    public void findProjectByIdTest(){
        Optional<ProjectDTO> project = projectDAO.findProjectWithEmployeeById(1);
        assertNotNull(project);
        assertTrue(project.isPresent());
        assertEquals(1,project.get().getProjectId());
    }

}
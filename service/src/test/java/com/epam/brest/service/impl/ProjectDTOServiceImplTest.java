package com.epam.brest.service.impl;

import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import com.epam.brest.test_db.SpringJdbcConfig;
import com.epam.brest.dao.ProjectJdbcDTODAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Import({ProjectDTOServiceImpl.class, ProjectJdbcDTODAO.class})
@ContextConfiguration(classes = SpringJdbcConfig.class)
@ComponentScan(basePackages = {"com.epam.brest.dao", "com.epam.brest.testdb"})
@PropertySource({"classpath:dao.properties"})
@Transactional
class ProjectDTOServiceImplTest {

    @Autowired
    private ProjectDTOService projectDTOService;

    @Test
    public void shouldFindAllProjects(){
        List<ProjectDTO> projectDTOList = projectDTOService.findAll(null);
        assertTrue(projectDTOList.size() > 0);
    }

    @Test
    public void shouldFindProject(){
        Optional<ProjectDTO> projectDTO  = projectDTOService.findById(1);
        assertTrue(projectDTO.isPresent());
    }

    @Test
    public void shouldNotFindProject(){
        Optional<ProjectDTO> projectDTO  = projectDTOService.findById(10);
        assertTrue(projectDTO.isEmpty());
    }

}
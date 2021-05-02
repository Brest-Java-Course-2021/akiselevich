package com.epam.brest.service.impl;

import com.epam.brest.ProjectDAO;
import com.epam.brest.model.Project;
import com.epam.brest.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private static final Logger LOGGER = LogManager.getLogger(ProjectServiceImpl.class);

    private final ProjectDAO projectDAO;

    public ProjectServiceImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public List<Project> findAll() {
        LOGGER.debug("Service method called to find all Project");
        return projectDAO.findAll();
    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        LOGGER.debug("Service method called to find Project by Id: {}", projectId);
        return projectDAO.findById(projectId);
    }

    @Override
    public Optional<Project> create(Project project) {
        LOGGER.debug("Service method called to create Project: {}", project);
        return projectDAO.create(project);
    }

    @Override
    public Optional<Project> update(Project project) {
        LOGGER.debug("Service method called to update Project with id: {}, new Project: {}", project.getProjectId(), project);
        return projectDAO.update(project);
    }

    @Override
    public void delete(Integer projectId) {
        LOGGER.debug("Service method called to delete Project by Id: {}", projectId);
        projectDAO.delete(projectId);
    }

}

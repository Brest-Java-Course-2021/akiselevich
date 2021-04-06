package com.epam.brest.service.impl;

import com.epam.brest.ProjectDAO;
import com.epam.brest.service.ProjectService;
import com.epam.brest.model.Project;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private static final Log LOGGER = LogFactory.getLog(ProjectServiceImpl.class);

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
        LOGGER.debug("Service method called to find Project by Id:" + projectId);
        return projectDAO.findById(projectId);
    }

    @Override
    public Optional<Project> create(Project project) {
        LOGGER.debug("Service method called to create Project:" + project.toString());
        return projectDAO.create(project);
    }

    @Override
    public Optional<Project> update(Project project) {
        LOGGER.debug("Service method called to update Project with id:" + project.getProjectId() + ", new Project:" + project.toString());
        return projectDAO.update(project);
    }

    @Override
    public void delete(Integer projectId) {
        LOGGER.debug("Service method called to delete Project by Id:" + projectId);
        projectDAO.delete(projectId);
    }

}

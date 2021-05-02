package com.epam.brest.service.impl;

import com.epam.brest.ProjectDTODAO;
import com.epam.brest.model.Filter;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectDTOServiceImpl implements ProjectDTOService {

    private static final Logger LOGGER = LogManager.getLogger(ProjectDTOServiceImpl.class);

    private final ProjectDTODAO projectDTODAO;

    public ProjectDTOServiceImpl(ProjectDTODAO projectDTODAO) {
        this.projectDTODAO = projectDTODAO;
    }

    @Override
    public List<ProjectDTO> findAll(Filter filter) {
        LOGGER.debug("Service method called to find all Project with employee count");
        return projectDTODAO.findAllProjectWithEmployeeCount(filter);
    }

    @Override
    public Optional<ProjectDTO> findById(Integer projectId) {
        LOGGER.debug("Service method called to find Project with employee count by Id: {}", projectId);
        return projectDTODAO.findProjectWithEmployeeById(projectId);
    }

}

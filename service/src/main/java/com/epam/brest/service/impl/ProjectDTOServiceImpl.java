package com.epam.brest.service.impl;

import com.epam.brest.ProjectDTODAO;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProjectDTOServiceImpl implements ProjectDTOService {

    private static final Log LOGGER = LogFactory.getLog(ProjectDTOServiceImpl.class);

    private final ProjectDTODAO projectDTODAO;

    public ProjectDTOServiceImpl(ProjectDTODAO projectDTODAO) {
        this.projectDTODAO = projectDTODAO;
    }

    @Override
    public List<ProjectDTO> findAll() {
        LOGGER.debug("Service method called to find all Project with employee count");
        return projectDTODAO.findAllProjectWithEmployeeCount();
    }

    @Override
    public Optional<ProjectDTO> findById(Integer projectId) {
        LOGGER.debug("Service method called to find Project with employee count by Id: " + projectId);
        return projectDTODAO.findProjectWithEmployeeById(projectId);
    }

}

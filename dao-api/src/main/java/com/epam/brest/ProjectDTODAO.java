package com.epam.brest;

import com.epam.brest.model.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectDTODAO {

    List<ProjectDTO> findAllProjectWithEmployeeCount();

    Optional<ProjectDTO> findProjectWithEmployeeById(Integer projectId);

}

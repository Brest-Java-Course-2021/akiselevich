package com.epam.brest;

import com.epam.brest.model.Filter;
import com.epam.brest.model.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectDTODAO {

    List<ProjectDTO> findAllProjectWithEmployeeCount(Filter filter);

    Optional<ProjectDTO> findProjectWithEmployeeById(Integer projectId);

}

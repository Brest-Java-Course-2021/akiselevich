package com.epam.brest.service;

import com.epam.brest.model.Filter;
import com.epam.brest.model.dto.ProjectDTO;

import java.util.List;
import java.util.Optional;

public interface ProjectDTOService {

    List<ProjectDTO> findAll(Filter filter);

    Optional<ProjectDTO> findById(Integer projectId);

}

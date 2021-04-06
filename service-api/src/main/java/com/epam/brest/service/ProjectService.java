package com.epam.brest.service;

import com.epam.brest.model.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    List<Project> findAll();

    Optional<Project> findById(Integer projectId);

    Optional<Project> create(Project project);

    Optional<Project> update(Project project);

    void delete(Integer projectId);

}

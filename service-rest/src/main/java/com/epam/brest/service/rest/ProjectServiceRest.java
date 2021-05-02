package com.epam.brest.service.rest;

import com.epam.brest.model.Project;
import com.epam.brest.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceRest implements ProjectService {

    private static final Logger LOGGER = LogManager.getLogger(ProjectServiceRest.class);

    private final String url;

    private final RestTemplate restTemplate;

    public ProjectServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Project> findAll() {
        LOGGER.debug("Rest service method called to find all Project");
        ResponseEntity responseEntity = restTemplate.getForEntity(url, List.class);
        return (List<Project>) responseEntity.getBody();
    }

    @Override
    public Optional<Project> findById(Integer projectId) {
        LOGGER.debug("Rest service method called to find Project by Id: {}", projectId);
        ResponseEntity<Project> responseEntity =
                restTemplate.getForEntity(url + "/" + projectId, Project.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<Project> create(Project project) {
        LOGGER.debug("Rest service method called to create Project: {}", project);
        ResponseEntity<Project> responseEntity = restTemplate.postForEntity(url, project, Project.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public Optional<Project> update(Project project) {
        LOGGER.debug("Rest service method called to update Project with id: {}, new Project: {}", project.getProjectId(), project);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Project> entity = new HttpEntity<>(project, headers);
        ResponseEntity<Project> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, entity, Project.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    @Override
    public void delete(Integer projectId) {
        LOGGER.debug("Rest service method called to delete Project by Id: {}", projectId);
        restTemplate.delete(url + "/" + projectId);
    }

}

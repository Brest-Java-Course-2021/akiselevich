package com.epam.brest.service.rest;

import com.epam.brest.model.Filter;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectDTOServiceRest implements ProjectDTOService {

    private static final Logger LOGGER = LogManager.getLogger(ProjectDTOServiceRest.class);

    private final String url;

    private final RestTemplate restTemplate;

    public ProjectDTOServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<ProjectDTO> findAll(Filter filter) {
        LOGGER.debug("Rest service method called to find all Project");
        ResponseEntity responseEntity = restTemplate.getForEntity(
                url + "?startDate={startDate}&finishDate={finishDate}",
                List.class,
                filter.getStartDate(),
                filter.getFinishDate());
        return (List<ProjectDTO>) responseEntity.getBody();
    }

    @Override
    public Optional<ProjectDTO> findById(Integer projectId) {
        LOGGER.debug("Rest service method called to find Project by Id: {}", projectId);
        ResponseEntity<ProjectDTO> responseEntity =
                restTemplate.getForEntity(url + "/" + projectId, ProjectDTO.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

}

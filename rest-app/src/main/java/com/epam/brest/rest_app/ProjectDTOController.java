package com.epam.brest.rest_app;

import com.epam.brest.model.Filter;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class ProjectDTOController {

    private static final Logger LOGGER = LogManager.getLogger(ProjectController.class);

    private final ProjectDTOService projectDTOService;

    public ProjectDTOController(ProjectDTOService projectDTOService) {
        this.projectDTOService = projectDTOService;
    }

    @GetMapping(value = "/project-dtos")
    public Collection<ProjectDTO> projectDtos(@RequestParam(value = "startDate", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                           @RequestParam(value = "finishDate", required = false)
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate finishDate) {
        LOGGER.debug("Controller method called to view all Projects");
        return projectDTOService.findAll(new Filter(startDate, finishDate));
    }

    @GetMapping(value = "/project-dtos/{id}")
    public ResponseEntity<ProjectDTO> findProjectDtoById(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to view Project by Id: {}", id);
        Optional<ProjectDTO> optional = projectDTOService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}

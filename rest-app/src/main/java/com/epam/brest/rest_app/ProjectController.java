package com.epam.brest.rest_app;

import com.epam.brest.model.Project;
import com.epam.brest.service.ProjectService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@CrossOrigin
@RestController
public class ProjectController {

    private static final Logger LOGGER = LogManager.getLogger(ProjectController.class);

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping(value = "/projects")
    public Collection<Project> projects() {
        LOGGER.debug("Controller method called to view all Projects");
        return projectService.findAll();
    }

    @GetMapping(value = "/projects/{id}")
    public ResponseEntity<Project> findById(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to view Project by Id: {}", id);
        Optional<Project> optional = projectService.findById(id);
        return optional.isPresent()
                ? new ResponseEntity<>(optional.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/projects", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        LOGGER.debug("Controller method called to create new Project: {}", project);
        return new ResponseEntity<>(projectService.create(project).get(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/projects", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        LOGGER.debug("Controller method called to update Project: {}", project);
        return new ResponseEntity<>(projectService.update(project).get(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/projects/{id}")
    public HttpStatus deleteProject(@PathVariable Integer id) {
        LOGGER.debug("Controller method called to delete Project with Id: {}", id);
        projectService.delete(id);
        return HttpStatus.OK;
    }

}

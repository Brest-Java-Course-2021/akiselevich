package com.epam.brest.web_app;

import com.epam.brest.model.Project;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.ProjectDTOService;
import com.epam.brest.service.ProjectService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class ProjectController {

    private static final Log LOGGER = LogFactory.getLog(ProjectController.class);

    private final ProjectDTOService projectDTOService;

    private final ProjectService projectService;

    public ProjectController(ProjectDTOService projectDTOService, ProjectService projectService) {
        this.projectDTOService = projectDTOService;
        this.projectService = projectService;
    }

    @GetMapping(value = "/projects")
    public final String projects(Model model) {
        LOGGER.debug("Controller method called to view all Project");
        model.addAttribute("projects", projectDTOService.findAll());
        return "projects";
    }

    @GetMapping(value = "/project/{id}")
    public final String gotoEditProjectPage(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to view EditPage Project with Id: " + id);
        Optional<ProjectDTO> optionalProject = projectDTOService.findById(id);
        if (optionalProject.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("project", optionalProject.get());
            return "project";
        } else {
            // TODO not found error
            return "redirect:projects";
        }
    }

    @GetMapping(value = "/project")
    public final String gotoAddProjectPage(Model model) {
        LOGGER.debug("Controller method called to view AddPage Project");
        model.addAttribute("isNew", true);
        model.addAttribute("project", new Project());
        return "project";
    }

    @PostMapping(value = "/project")
    public String addProject(Project project) {
        LOGGER.debug("Controller method called to add new Project: " + project.toString());
        projectService.create(project);
        return "redirect:/projects";
    }

    @PostMapping(value = "/project/{id}")
    public String updateProject(Project project) {
        LOGGER.debug("Controller method called to update Project: " + project.toString());
        projectService.update(project);
        return "redirect:/projects";
    }

    @GetMapping(value = "/project/{id}/delete")
    public final String deleteProjectById(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to delete Project: " + id);
        projectService.delete(id);
        return "redirect:/projects";
    }

}

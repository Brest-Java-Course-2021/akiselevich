package com.epam.brest.web_app;

import com.epam.brest.model.Filter;
import com.epam.brest.model.Project;
import com.epam.brest.model.dto.EmployeeDTO;
import com.epam.brest.model.dto.ProjectDTO;
import com.epam.brest.service.EmployeeService;
import com.epam.brest.service.ProjectDTOService;
import com.epam.brest.service.ProjectService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProjectController {

    private static final Log LOGGER = LogFactory.getLog(ProjectController.class);

    private final ProjectDTOService projectDTOService;
    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectDTOService projectDTOService, ProjectService projectService, EmployeeService employeeService) {
        this.projectDTOService = projectDTOService;
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @GetMapping(value = "/projects")
    public final String projects(Model model,
                                 @RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "finishDate", required = false) String finishDate) {
        try {
            LOGGER.debug("Controller method called to view all Project");
            DateTimeFormatter format = DateTimeFormatter.ISO_DATE_TIME;
            model.addAttribute(
                    "projects",
                    projectDTOService.findAll(
                            new Filter(
                                    startDate == null || startDate.equals("")
                                            ? null
                                            : LocalDateTime.parse(startDate, format),
                                    finishDate == null || finishDate.equals("")
                                            ? null
                                            : LocalDateTime.parse(finishDate, format)))
            );
            model.addAttribute("filter", new Filter());
            return "projects";
        }catch (DateTimeParseException ex){
            model.addAttribute("statusCodeName", "Bad Request");
            model.addAttribute("statusCode", "400".toCharArray());
            model.addAttribute("errorMessage", "filter param should be format: yyyy-MM-ddThh:mm:ss");
            return "error";
        }
    }

    @GetMapping(value = "/project/{id}")
    public final String gotoEditProjectPage(@PathVariable Integer id, Model model) {
        LOGGER.debug("Controller method called to view EditPage Project with Id: " + id);
        Optional<ProjectDTO> optionalProject = projectDTOService.findById(id);
        if (optionalProject.isPresent()) {
            model.addAttribute("isNew", false);
            model.addAttribute("project", new Project(
                    optionalProject.get().getProjectId(),
                    optionalProject.get().getProjectName(),
                    optionalProject.get().getStartDate(),
                    optionalProject.get().getFinishDate(),
                    optionalProject.get().getEmployees() == null
                            ? null
                            : optionalProject.get().getEmployees()
                                .stream()
                                .map(EmployeeDTO::getEmployeeId)
                                .collect(Collectors.toList())));
            model.addAttribute("employeeList", employeeService.findAll());
            return "project";
        } else {
            model.addAttribute("statusCodeName", "Bad Request");
            model.addAttribute("statusCode", "400".toCharArray());
            model.addAttribute("errorMessage", "Project with id: " + id + ", not found");
            return "error";
        }
    }

    @GetMapping(value = "/project")
    public final String gotoAddProjectPage(Model model) {
        LOGGER.debug("Controller method called to view AddPage Project");
        model.addAttribute("isNew", true);
        model.addAttribute("project", new Project());
        model.addAttribute("employeeList", employeeService.findAll());
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

package com.epam.brest.model.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class ProjectDTO {
    private Integer projectId;
    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private List<EmployeeDTO> employees;
    private Integer numberOfEmployees;

    public ProjectDTO() {
    }

    public ProjectDTO(Integer projectId, String projectName, LocalDateTime startDate, LocalDateTime finishDate, List<EmployeeDTO> employees, Integer numberOfEmployees) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.employees = employees;
        this.numberOfEmployees = numberOfEmployees;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public Integer getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(Integer numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectDTO that = (ProjectDTO) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(projectName, that.projectName) && Objects.equals(startDate, that.startDate) && Objects.equals(finishDate, that.finishDate) && Objects.equals(employees, that.employees) && Objects.equals(numberOfEmployees, that.numberOfEmployees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, startDate, finishDate, employees, numberOfEmployees);
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", employees=" + employees +
                ", numberOfEmployees=" + numberOfEmployees +
                '}';
    }
}

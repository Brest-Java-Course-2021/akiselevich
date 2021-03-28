package com.epam.brest.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Project {
    private Integer projectId;
    private String projectName;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private List<Integer> employeeId;

    public Project() {
    }

    public Project(Integer projectId, String projectName, LocalDateTime startDate, LocalDateTime finishDate, List<Integer> employeeId) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.employeeId = employeeId;
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

    public List<Integer> getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(List<Integer> employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(projectId, project.projectId) && Objects.equals(projectName, project.projectName) && Objects.equals(startDate, project.startDate) && Objects.equals(finishDate, project.finishDate) && Objects.equals(employeeId, project.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, projectName, startDate, finishDate, employeeId);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", projectName='" + projectName + '\'' +
                ", startDate=" + startDate +
                ", finishDate=" + finishDate +
                ", employeeId=" + employeeId +
                '}';
    }
}

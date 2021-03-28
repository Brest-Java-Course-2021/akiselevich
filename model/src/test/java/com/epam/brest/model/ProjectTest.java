package com.epam.brest.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProjectTest {

    @Test
    public void EmptyProjectToStringTest(){
        Project project = new Project();
        assertEquals("Project{projectId=null, projectName='null', startDate=null, finishDate=null, employeeId=null}",project.toString());
    }

    @Test
    public void ProjectToStringTest(){
        LocalDateTime currentTime = LocalDateTime.of(2015, Month.JULY, 29, 19, 30, 40);
        Project project = new Project(1,"abc",currentTime,currentTime, null);
        assertEquals("Project{projectId=1, projectName='abc', startDate=2015-07-29T19:30:40, finishDate=2015-07-29T19:30:40, employeeId=null}",project.toString());
    }
}
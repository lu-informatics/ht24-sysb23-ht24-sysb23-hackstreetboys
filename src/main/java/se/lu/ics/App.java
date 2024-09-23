package se.lu.ics;

import java.io.IOException;
import java.util.List;

import se.lu.ics.data.ProjectDao;
import se.lu.ics.models.Project;

public class App {
    public static void main(String[] args) {

        try{
            ProjectDao projectDao = new ProjectDao();
            List<Project> projects = projectDao.findAllProjects();
            //Print name of all projects
            for (Project project : projects) {
                System.out.println(project.getProjectName());
            }
        } catch (IOException e) {
            System.out.println("Could not connect to the database: " + e.getMessage());
        }
    }
}
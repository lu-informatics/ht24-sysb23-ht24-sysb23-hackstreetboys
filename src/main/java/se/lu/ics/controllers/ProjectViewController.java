package se.lu.ics.controllers;

import javafx.fxml.Initializable;
import se.lu.ics.models.Project;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectViewController implements Initializable {

    private Project project;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set up project table view
        

    }

    public void setProject(Project project) {
        this.project = project;
    }

}

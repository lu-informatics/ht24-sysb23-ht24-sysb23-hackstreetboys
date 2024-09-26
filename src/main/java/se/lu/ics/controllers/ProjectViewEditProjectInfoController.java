package se.lu.ics.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import se.lu.ics.models.Project;

public class ProjectViewEditProjectInfoController {


    //set project method

    Project project = new Project();

  public void setProject(Project project) {
        this.project = project;
        // Populate the fields with project data
        textFieldProjectNo.setText(project.getProjectNo());
        textFieldProjectName.setText(project.getProjectName());
        datePickerProjectStartDate.setValue(project.getStartDate());
        datePickerProjectEndDate.setValue(project.getEndDate());
    }
    @FXML
    private Button btnCancelProjectEdit;

    @FXML
    private Button btnSaveProjectEdit;

    @FXML
    private DatePicker datePickerProjectEndDate;

    @FXML
    private DatePicker datePickerProjectStartDate;

    @FXML
    private Label labelWarning;

    @FXML
    private Pane paneWarning;

    @FXML
    private TextField textFieldProjectName;

    @FXML
    private TextField textFieldProjectNo;

    @FXML
    void handleBtnCancelProjectEdit(ActionEvent event) {

    }

    @FXML
    void handleBtnSaveProject(ActionEvent event) {

    }

}

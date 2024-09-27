package se.lu.ics.controllers;

import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import se.lu.ics.models.Project;
import se.lu.ics.data.ProjectDao;

public class ProjectViewEditProjectInfoController {

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
    private Text textForProjectNo;
    
    private ProjectViewController projectViewController;
    private MainViewController mainViewController;
    private Project project;
    private ProjectDao projectDao;


    public ProjectViewEditProjectInfoController() {
         try {
            this.projectDao = new ProjectDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Setters
    public void setProject(Project project) {
        this.project = project;
        // Populate the fields with project data
        textForProjectNo.setText(project.getProjectNo());
        textFieldProjectName.setText(project.getProjectName());
        datePickerProjectStartDate.setValue(project.getStartDate());
        datePickerProjectEndDate.setValue(project.getEndDate());
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void setProjectViewController(ProjectViewController projectViewController) {
        this.projectViewController = projectViewController;
    }

    // Cancel the project edit, close window
    @FXML
    void handleBtnCancelProjectEdit(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) btnCancelProjectEdit.getScene().getWindow();
        stage.close();

    }

    // Save the project edit
    @FXML
    void handleBtnSaveProject(ActionEvent event) {
        try {
            // Retrieve updated project information from input fields
            String projectNo = textForProjectNo.getText();
            String projectName = textFieldProjectName.getText();
            LocalDate startDate = datePickerProjectStartDate.getValue();
            LocalDate endDate = datePickerProjectEndDate.getValue();

            // Validate endDate is after startDate
            if (endDate.isBefore(startDate)) {
                displayErrorMessage("End date must be after start date.");
                return;
            }
    
            // Update the project object with new information
            project.setProjectNo(projectNo);
            project.setProjectName(projectName);
            project.setStartDate(startDate);
            project.setEndDate(endDate);
    
            // Save the updated project information to the database
            projectDao.updateProject(project);
    
            // Close the current window
            Stage stage = (Stage) btnSaveProjectEdit.getScene().getWindow();
            stage.close();
    
            // Refresh the project view to reflect changes
            if (projectViewController != null) {
                projectViewController.setProject(project);
                projectViewController.updateTableView();
            } else {
                displayErrorMessage("Project view controller is not set.");
            }
    
            // Update the main view controller to reflect changes
            if (mainViewController != null) {
                mainViewController.updateProjectsTableView();
            }
    
        } catch (Exception e) {
            // Handle exception, show error message or log the error
            displayErrorMessage("Error occurred while saving project information: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Display error message
    private void displayErrorMessage(String message) {
        paneWarning.setVisible(true);
        labelWarning.setText(message);
    }
}




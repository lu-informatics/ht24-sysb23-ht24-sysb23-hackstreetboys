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
import se.lu.ics.controllers.ProjectViewController;
import se.lu.ics.data.DaoException;
import se.lu.ics.data.ProjectDao;
import se.lu.ics.data.MilestoneDao;
import se.lu.ics.models.Project;


public class MilestoneAddController {
    private ProjectViewController projectViewController;
    private Project project;
    private MilestoneDao milestoneDao;

    // Method to set the project
    public void setProject(Project project) {
        this.project = project;
        textProjectNo.setText(project.getProjectNo());
    }

    public MilestoneAddController() {
        try {
            this.milestoneDao = new MilestoneDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to set the main view controller
    public void setMainViewController(ProjectViewController mainViewController) {
        this.projectViewController = mainViewController;
    }

    @FXML
    private Button btnCancelMilestoneAdd;

    @FXML
    private Button btnSaveMilestone;

    @FXML
    private DatePicker datePickerMilestoneDate;

    @FXML
    private Label labelWarning;

    @FXML
    private Pane paneWarning;

    @FXML
    private TextField textFieldMilestoneDescription;

    @FXML
    private TextField textFieldMilestoneNo;

    @FXML
    private Text textProjectNo;

    @FXML
    void handleBtnCancelMilestoneAdd(ActionEvent event) {
        // Close the window
        Stage stage = (Stage) btnCancelMilestoneAdd.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleBtnSaveMilestone(ActionEvent event) {

    }

}